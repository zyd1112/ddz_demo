package com.zyd.ddz.service.impl;

import com.zyd.ddz.constant.CharacterType;
import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomManagerFactory;
import com.zyd.ddz.room.AbstractRoomManager;
import com.zyd.ddz.service.RoomService;
import com.zyd.ddz.utils.GameLogicUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;

import java.util.ArrayList;
import java.util.List;

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
        Player player = new Player();
        player.setJoyBeans(userDomain.getJoyBeans());
        player.setUid(uid);
        player.setSession(session);
        player.setName(userDomain.getNickname());
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
        if(room == null || !room.isStart()){
            return;
        }
        List<Card> curTableCars = room.getCurTableCars();
        List<Card> cardList = player.getCardList();
        if(cardList.size() < cards.size() || !GameLogicUtils.check(cards, curTableCars)){
            logger.info("{} 出的牌不符合规则, cardList: {}, cast: {}, curTableCars: {}", uid, cardList, cards, curTableCars);
            return;
        }
        room.setCurTableCars(new ArrayList<>(cards));
        cardList.removeIf(cards::contains);

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

}
