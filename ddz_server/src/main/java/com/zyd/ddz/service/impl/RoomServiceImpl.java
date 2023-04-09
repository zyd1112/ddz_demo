package com.zyd.ddz.service.impl;

import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomFactory;
import com.zyd.ddz.room.AbstractRoomEvent;
import com.zyd.ddz.service.RoomService;
import com.zyd.ddz.utils.IdManager;
import com.zyd.ddz.utils.RandomUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static xyz.noark.log.LogHelper.logger;

/**
 * @author zyd
 * @date 2023/4/7 17:00
 */
@Service
public class RoomServiceImpl implements RoomService {

    private static final AtomicInteger ROOM_NUM = new AtomicInteger(0);

    /**
     * 可以被分配的房间
     */
    private final List<Room> availableRoom = new CopyOnWriteArrayList<>();

    /**
     * roomType -> (roomId -> room)
     */
    private final Map<Integer, Map<Long, Room>> roomMap = new ConcurrentHashMap<>();

    /**
     * roleId -> roomId
     */
    private final Map<Long, Long> playerToRoomMap = new ConcurrentHashMap<>();
    @Autowired
    UserDao userDao;
    @Autowired
    IdManager idManager;

    @Override
    public boolean enterRoom(Session session, long uid, int roomType) {
        AbstractRoomEvent abstractRoomEvent = RoomFactory.getRoom(roomType);
        UserDomain userDomain = userDao.cacheGet(uid);
        if (abstractRoomEvent == null || userDomain == null){
            return false;
        }
        Long roomId = playerToRoomMap.get(uid);
        Map<Long, Room> rooms = roomMap.computeIfAbsent(roomType, type -> new ConcurrentHashMap<>());
        Room room;
        if(availableRoom.isEmpty()){
            roomId = idManager.generator();
            playerToRoomMap.put(uid, roomId);
            room = new Room();
            room.setAbstractRoomEvent(abstractRoomEvent);
            room.setCreateTime(System.currentTimeMillis());
            room.setId(roomId);
            room.setName("房间-" + ROOM_NUM.incrementAndGet());
            rooms.put(roomId, room);
            abstractRoomEvent.onCreate(room);
            availableRoom.add(room);
            roomMap.computeIfAbsent(roomType, type -> new ConcurrentHashMap<>());
        }
        room = availableRoom.get(RandomUtils.getRandomNum(availableRoom.size()));

        Player player = new Player();
        player.setRoomId(roomId);
        player.setUid(uid);
        player.setJoyBeans(userDomain.getJoyBeans());
        room.getPlayerList().add(player);
        abstractRoomEvent.onPlayerEnter(room, player);
        return true;
    }

    @Override
    public List<Room> getRoom() {
        List<Room> roomList = new ArrayList<>();
        this.roomMap.forEach((k, map) -> {
            roomList.addAll(map.values());
        });
        return roomList;
    }

    @Override
    public List<Room> getAvailableRoom() {
        return this.availableRoom;
    }

    @Override
    public void exitRoom(Session session, long uid, long roomId) {
        if (!this.playerToRoomMap.containsKey(uid)) {
            return;
        }
        this.roomMap.forEach((type, roomMap) -> {
            Room room = roomMap.get(roomId);
            if(room != null){
                List<Player> playerList = room.getPlayerList();
                Player player = null;
                int index = 0;
                for (Player p : playerList) {
                    if(p.getUid() == uid){
                        player = p;
                    }
                    index++;
                }
                if(player == null) return;

                logger.info("{} 玩家 离开房间: {}", uid, roomId);

                if(room.getGameStartTime() > 0){
                    playerList.remove(index);
                }else{
                    player.setAuto(true);
                }
            }
        });
    }

    @Override
    public void destroyRoom(Room room) {
        long id = room.getId();
        this.roomMap.values().forEach((map) -> map.remove(id));
        for (Player player : room.getPlayerList()) {
            this.playerToRoomMap.remove(player.getUid());
        }
    }

}
