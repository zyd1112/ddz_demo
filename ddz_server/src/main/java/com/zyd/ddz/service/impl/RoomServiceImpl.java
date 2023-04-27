package com.zyd.ddz.service.impl;

import com.zyd.ddz.constant.CharacterType;
import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomManagerFactory;
import com.zyd.ddz.message.room.dto.PlayerDto;
import com.zyd.ddz.message.room.response.*;
import com.zyd.ddz.room.AbstractRoomManager;
import com.zyd.ddz.service.RoomService;
import com.zyd.ddz.utils.GameLogicUtils;
import com.zyd.ddz.utils.MessageUtils;
import com.zyd.ddz.utils.TimeUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;
import xyz.noark.core.network.SessionManager;

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
    public boolean enterRoom(long uid, int roomType) {
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
            player.setSuggestOffset(-1);
            player.setName(userDomain.getNickname());
            player.setImageIndex(userDomain.getImageIndex());
        }
        roomManager.onPlayerEnter(player);
        return true;
    }


    @Override
    public void exitRoom(long uid, int roomType) {
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
    public void playerReady(long uid, int roomType) {
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
    public void playerStart(long uid, int roomType) {
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

        roomManager.onGameStart(room);
    }

    @Override
    public void scramble(long uid, int roomType, boolean scramble) {
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
        if(scramble){
            multiple <<= 1;
            player.setScrambleCount(player.getScrambleCount() + 1);
        }
        int scrambleCount = room.getScrambleCount();
        scrambleCount++;
        boolean success = scrambleCount / roomManager.getSize() == 2;
        if(success){
            List<Player> players = new ArrayList<>(room.getPlayers().values());
            players.sort((p1, p2) -> p2.getScrambleCount() - p1.getScrambleCount());
            for (int i = 0; i < players.size(); i++) {
                Player curPlayer = players.get(i);
                if (i == 0) {
                    curPlayer.setCharacter(CharacterType.LANDOWNER);
                    room.setNextPlayer(curPlayer);
                    for (Card downCard : room.getDownCards()) {
                        downCard.setSend(true);
                    }
                    List<Card> cardList = curPlayer.getCardList();
                    cardList.addAll(room.getDownCards());
                    cardList.sort((o1, o2) -> {
                        if (o1.getCardValue() == o2.getCardValue()) {
                            return o1.getShape() - o2.getShape();
                        }
                        return o1.getCardValue() - o2.getCardValue();
                    });
                } else {
                    curPlayer.setCharacter(room.getCharacterTypeList().remove(0));
                }
            }
            room.setCharacterInit(true);
        }
        room.initClock();
        room.setScrambleCount(scrambleCount);
        room.setMultiple(multiple);
        if(!success){
            room.setNextPlayer(getNext(room, player));
        }
        ResPlayerCharacterMessage message = new ResPlayerCharacterMessage();
        room.getPlayers().forEach((id, p) -> {
            message.getCharacter().put(id, p.getCharacter().getType());
            message.getCardsMap().put(id, p.getCardList());
        });
        message.setMultiple(multiple);
        message.setUid(uid);
        message.setSuccess(success);
        message.setStatus(scramble);
        MessageUtils.sendMessageForRoom(room, message);
    }

    @Override
    public void sendCard(long uid, int roomType, List<Card> cards) {
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
        room.initClock();
        room.setCurPlayer(player);
        room.setNextPlayer(getNext(room, player));
        player.setSendCard(cards);
        cardList.removeIf(cards::contains);
        if(cardList.size() == 0){
            roomManager.onGameOver(room);
        }
        sendCardMessage(room, cards, uid);
        logger.info("[{}: {}] 玩家出牌: cards: {}", uid, player.getName(), cards);
    }

    @Override
    public void noSend(long uid, int roomType) {
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
        room.initClock();
        Player next = getNext(room, player);
        room.setNextPlayer(next);
        Player curPlayer = room.getCurPlayer();
        if(curPlayer != null && next.getUid() == curPlayer.getUid()){
            curPlayer = null;
            room.setCurPlayer(null);
        }
        sendCardMessage(room, curPlayer == null ? new ArrayList<>() : curPlayer.getSendCard() , uid);
    }

    private Player getNext(Room room, Player player) {
        List<Player> players = room.getPlayers().values().stream()
                .sorted(Comparator.comparing(Player::getEnterTime)).collect(Collectors.toList());
        int index = players.indexOf(player);
        return  players.get(index < players.size() - 1 ? index + 1 : 0);
    }

    @Override
    public void suggest(long uid, int roomType) {
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
        if(curPlayer == null){
            return;
        }
        ResPlayerSuggestMessage message = new ResPlayerSuggestMessage();
        List<List<Card>> availableCards = GameLogicUtils.getAvailableCards(player.getCardList(), curPlayer.getSendCard());
        if(availableCards.isEmpty()){
            noSend(uid, roomType);
            return;
        }
        int suggestOffset = player.getSuggestOffset();
        if(suggestOffset < availableCards.size() - 1){
            suggestOffset++;
        }else {
            suggestOffset = 0;
        }
        List<Card> list = availableCards.isEmpty() ? Collections.emptyList() : availableCards.get(suggestOffset);
        player.setSuggestOffset(suggestOffset);
        message.setAvailableCards(list);
        MessageUtils.sendMessage(uid, message);
    }

    @Override
    public void reqCountdown(long uid, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        ResRoomTimeHeartMessage message = new ResRoomTimeHeartMessage();
//        message.setTime(TimeUtils.getNowTimeMillis());
        MessageUtils.sendMessageForRoom(room, message);
    }

    @Override
    public void autoSend(Room room, Player player) {
        if (room.isCharacterInit()){
            Player curPlayer = room.getCurPlayer();

            if(curPlayer == null){
                ArrayList<Card> list = new ArrayList<>();
                list.add(player.getCardList().get(0));
                sendCard(player.getUid(), room.getType(), list);
                return;
            }
            List<List<Card>> availableCards = GameLogicUtils.getAvailableCards(player.getCardList(), curPlayer.getSendCard());
            if(availableCards.isEmpty()){
                noSend(player.getUid(), room.getType());
            }else {
                sendCard(player.getUid(), room.getType(), availableCards.get(0));
            }
        }else {
            scramble(player.getUid(), room.getType(), false);
        }

    }

    @Override
    public void sendRewards(Room room) {
        room.getPlayers().forEach((uid, p) -> {
            UserDomain userDomain = userDao.cacheGet(uid);
            if(userDomain == null){
                return;
            }
            userDomain.setJoyBeans(p.getJoyBeans());
            userDao.update(userDomain);
        });
    }

    @Override
    public void playerLeave(long uid, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        if (!roomManager.getPlayers().containsKey(uid)) {
            return;
        }
        Player player = roomManager.getPlayers().get(uid);
        Room room = roomManager.getRoom(player.getRoomId());
        roomManager.onPlayerExit(room, player);
    }

    @Override
    public void autoRobot(long uid, int roomType, boolean choose) {
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
        message.setFirstId(room.getCurPlayer() == null ? room.getNextPlayer().getUid() : room.getCurPlayer().getUid());

        players.forEach((playerId, p) -> {
            p.setSuggestOffset(-1);
            message.getCardsMap().computeIfAbsent(p.getUid(), k -> new ArrayList<>(p.getCardList()));
        });
        MessageUtils.sendMessageForRoom(room, message);
    }
}
