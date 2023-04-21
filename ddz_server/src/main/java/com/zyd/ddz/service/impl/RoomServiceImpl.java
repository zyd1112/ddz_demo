package com.zyd.ddz.service.impl;

import com.zyd.ddz.constant.CharacterType;
import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomManagerFactory;
import com.zyd.ddz.message.Message;
import com.zyd.ddz.message.room.ResPlayerCardMessage;
import com.zyd.ddz.message.room.ResPlayerReadyMessage;
import com.zyd.ddz.message.room.ResPlayerSuggestMessage;
import com.zyd.ddz.message.room.ResRoomTimeMessage;
import com.zyd.ddz.message.room.dto.PlayerDto;
import com.zyd.ddz.room.AbstractRoomManager;
import com.zyd.ddz.service.RoomService;
import com.zyd.ddz.utils.GameLogicUtils;
import com.zyd.ddz.utils.MessageUtils;
import com.zyd.ddz.utils.TimeUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;

import java.util.*;
import java.util.stream.Collectors;

import static xyz.noark.log.LogHelper.logger;
/**
 * @author zyd
 * @date 2023/4/7 17:00
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    UserDao userDao;

    @Override
    public boolean enterRoom(Session session, long uid, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return false;
        }
        UserDomain userDomain = userDao.cacheGet(uid);
        if(userDomain == null){
            return false;
        }
        Player player = roomManager.getPlayers().get(uid);
        if(player == null){
            player = new Player();
            player.setJoyBeans(userDomain.getJoyBeans());
            player.setUid(uid);
            player.setSession(session);
            player.setSuggestOffset(-1);
            player.setName(userDomain.getNickname());
        }
        roomManager.onPlayerEnter(player);
        return true;
    }


    @Override
    public void exitRoom(Session session, long uid, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        if(room == null){
            return;
        }
        roomManager.onPlayerExit(room, player);
    }

    @Override
    public void playerReady(Session session, long uid, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        if(room == null){
            return;
        }
        player.setReady(!player.isReady());
        ResPlayerReadyMessage message = new ResPlayerReadyMessage();
        for (Player p : room.getPlayers().values()) {
            PlayerDto playerDto = new PlayerDto();
            playerDto.setUid(p.getUid());
            playerDto.setRoomHost(p.isRoomHost());
            playerDto.setReady(p.isReady());
            message.getPlayerDtoList().add(playerDto);
        }
        MessageUtils.sendMessageForRoom(room, message);
        roomManager.onPlayerReady(room, player, player.isReady());
    }

    @Override
    public void playerStart(Session session, long uid, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        if(room == null || room.isStart()){
            return;
        }
        Map<Long, Player> players = room.getPlayers();
        if(players.size() < roomManager.getSize()){
            return;
        }
        for (Player p : players.values()) {
            if(!p.isReady()){
                return;
            }
        }
        room.setStart(true);

        roomManager.onGameStart(room);
    }

    @Override
    public void scramble(Session session, long uid, int roomType, boolean scramble) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        if(room == null || !room.isStart()){
            return;
        }
        int multiple = room.getMultiple();
        int scrambleCount = player.getScrambleCount();
        if(scramble){
            multiple <<= 1;
        }
        if(scrambleCount == 2){
            player.setCharacter(scramble ? CharacterType.LANDOWNER : room.getCharacterTypeList().remove(0));
        }

        room.setMultiple(multiple);
        player.setScrambleCount(scrambleCount + 1);

    }

    @Override
    public void sendCard(Session session, long uid, int roomType, List<Card> cards) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        if(room == null || !room.isStart()){
            return;
        }

        Player curPlayer = room.getCurPlayer();
        List<Card> cardList = player.getCardList();
        List<Card> curCards = curPlayer == null ? null : curPlayer.getSendCard();
        if(!GameLogicUtils.check(cards, curCards)){
            logger.info("{} 出的牌不符合规则, cardList: {}, cast: {}, curTableCars: {}", uid, cardList, cards, curCards);
            return;
        }
        room.setCurPlayer(player);
        room.setNextPlayer(getNext(room, player));
        player.setSendCard(cards);
        cardList.removeIf(cards::contains);
        if(cardList.size() == 0){
            room.setGameOver(true);
            room.setGameOverTime(TimeUtils.getNowTimeMillis());

            roomManager.onGameOver(room);
        }
        sendCardMessage(room, cards, uid);
        logger.info("[{}: {}] 玩家出牌: cards: {}", uid, player.getName(), cards);
    }

    @Override
    public void noSend(Session session, long uid, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        if(room == null || !room.isStart()){
            return;
        }
        Player nextPlayer = room.getNextPlayer();
        if(nextPlayer != null && player.getUid() != nextPlayer.getUid()){
            return;
        }
        Player next = getNext(room, player);
        room.setNextPlayer(next);
        Player curPlayer = room.getCurPlayer();
        if(curPlayer != null && next.getUid() == curPlayer.getUid()){
            room.setCurPlayer(null);
        }
        sendCardMessage(room, new ArrayList<>(), uid);
    }

    private Player getNext(Room room, Player player) {
        List<Player> players = room.getPlayers().values().stream().sorted(Comparator.comparing(Player::getCharacter)).collect(Collectors.toList());
        int index = players.indexOf(player);
        return  players.get(index < players.size() - 1 ? index + 1 : 0);
    }

    @Override
    public void suggest(Session session, long uid, int roomType, boolean send) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
//        if(room == null || !room.isStart()){
//            return;
//        }
        Player curPlayer = room.getCurPlayer();
        if(curPlayer == null){
            if(send){
                List<Card> list = new ArrayList<>();
                list.add(player.getCardList().get(0));
                sendCard(session, uid, roomType, list);
            }
            return;
        }
        ResPlayerSuggestMessage message = new ResPlayerSuggestMessage();
        List<List<Card>> availableCards = GameLogicUtils.getAvailableCards(player.getCardList(), curPlayer.getSendCard());

        int suggestOffset = player.getSuggestOffset();
        if(suggestOffset < availableCards.size() - 1){
            suggestOffset++;
        }else {
            suggestOffset = 0;
        }
        List<Card> list = availableCards.isEmpty() ? Collections.emptyList() : availableCards.get(suggestOffset);
        if(send){
            if(list.isEmpty()){
                noSend(session, uid, roomType);
            }else {
                sendCard(session, uid, roomType, list);
            }
            return;
        }
        player.setSuggestOffset(suggestOffset);
        message.setAvailableCards(list);
        MessageUtils.sendMessage(session, message);
    }

    @Override
    public void reqCountdown(Session session, long uid, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        ResRoomTimeMessage message = new ResRoomTimeMessage();
        message.setTime(TimeUtils.getNowTimeMillis());
        MessageUtils.sendMessageForRoom(room, message);
    }

    @Override
    public void autoRobot(Session session, long uid, int roomType, boolean choose) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        if(room == null || !room.isStart()){
            return;
        }
        player.setAuto(choose);
    }


    private void sendCardMessage(Room room, List<Card> removes, long uid){
        Map<Long, Player> players = room.getPlayers();
        ResPlayerCardMessage message = new ResPlayerCardMessage();
        message.setType(1);
        message.setUid(uid);
        message.getRemoveCards().addAll(removes);
        message.setNextId(room.getNextPlayer().getUid());
        message.setFirstId(room.getCurPlayer() == null ? message.getNextId() : room.getCurPlayer().getUid());

        players.forEach((playerId, p) -> {
            p.setSuggestOffset(-1);
            message.getCardsMap().computeIfAbsent(p.getUid(), k -> new ArrayList<>(p.getCardList()));
        });
        MessageUtils.sendMessageForRoom(room, message);
    }
}
