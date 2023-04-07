package com.zyd.ddz.service.impl;

import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomFactory;
import com.zyd.ddz.room.RoomEvent;
import com.zyd.ddz.service.RoomService;
import com.zyd.ddz.utils.IdManager;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zyd
 * @date 2023/4/7 17:00
 */
@Service
public class RoomServiceImpl implements RoomService {

    private static final AtomicInteger ROOM_NUM = new AtomicInteger(0);

    /**
     * roomType -> (roomId -> Room)
     */
    private final Map<Integer, Map<Long, Room>> roomRoleMap = new ConcurrentHashMap<>();

    /**
     * roleId -> roomId
     */
    private final Map<Long, Long> roleToRoomMap = new ConcurrentHashMap<>();
    @Autowired
    UserDao userDao;
    @Autowired
    IdManager idManager;

    @Override
    public boolean enterRoom(Session session, long uid, int roomType) {
        RoomEvent roomEvent = RoomFactory.getRoom(roomType);
        if (roomEvent == null){
            return false;
        }
        Long roomId = roleToRoomMap.get(uid);
        Map<Long, Room> roomMap = roomRoleMap.computeIfAbsent(roomType, type -> new ConcurrentHashMap<>());
        Room room;
        if(roomId == null){
            roomId = idManager.generator();
            roleToRoomMap.put(uid, roomId);
            room = roomMap.computeIfAbsent(roomId, k -> new Room());
            room.setRoomEvent(roomEvent);
            room.setCreateTime(System.currentTimeMillis());
            room.setId(roomId);
            room.setName("房间-" + ROOM_NUM.incrementAndGet());
            roomEvent.onCreate(room);
        }
        room = roomMap.get(roomId);
        if(room.getRoleList().size() == roomEvent.getSize()){
            return false;
        }
        room.getRoleList().add(uid);
        roomEvent.onEnter(room);

        return true;
    }
}
