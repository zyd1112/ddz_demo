package com.zyd.ddz.service.impl;

import com.zyd.ddz.constant.CharacterType;
import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomManagerFactory;
import com.zyd.ddz.message.room.ResPlayerCardMessage;
import com.zyd.ddz.room.AbstractRoomManager;
import com.zyd.ddz.service.RoomService;
import com.zyd.ddz.utils.GameLogicUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            player.setName(userDomain.getNickname());
        }
        Map<Integer, List<Card>> cards = GameLogicUtils.sendCard();
        logger.info("{}", cards);
        player.setCardList(new ArrayList<>(cards.get(1)));

        roomManager.onPlayerEnter(player);
        sendCardMessage(roomManager.getRoom(player.getRoomId()).getPlayers(), new ArrayList<>());
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
    public void playerReady(Session session, long uid, int roomType, boolean ready) {
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
        player.setReady(ready);
        roomManager.onPlayerReady(room, player, ready);
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
//        if(room == null || !room.isStart()){
//            return;
//        }
        Player curPlayer = room.getCurPlayer();
        List<Card> cardList = player.getCardList();
        List<Card> curCards = curPlayer == null ? null : curPlayer.getSendCard();
        if(!GameLogicUtils.check(cards, curCards)){
            logger.info("{} 出的牌不符合规则, cardList: {}, cast: {}, curTableCars: {}", uid, cardList, cards, curCards);
            return;
        }
        room.setCurPlayer(player);
        player.setSendCard(cards);
        cardList.removeIf(cards::contains);
        sendCardMessage(roomManager.getRoom(player.getRoomId()).getPlayers(), cards);
        logger.info("[{}: {}] 玩家出牌: cards: {}", uid, player.getName(), cards);
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


    private void sendCardMessage(Map<Long, Player> players, List<Card> removes){
        ResPlayerCardMessage message = new ResPlayerCardMessage();
        message.setType(1);
        message.getRemoveCards().addAll(removes);
        players.forEach((playerId, p) -> message.getCardsMap().computeIfAbsent((p.getCharacter().ordinal() + 1), k -> new ArrayList<>(p.getCardList())));
        players.forEach((playerId, p) -> p.getSession().send(message.getOpcode(), message));
    }
}
