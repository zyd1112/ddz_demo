package com.zyd.ddz.service;

import com.zyd.ddz.entity.Room;
import xyz.noark.core.network.Session;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/7 16:59
 */
public interface RoomService {

    boolean enterRoom(Session session, long uid, int roomType);

    void exitRoom(Session session, long uid, long roomId);

    void destroyRoom(Room room);

    void onGameStart(Room room);

    List<Room> getRoom();

    List<Room> getAvailableRoom();
}
