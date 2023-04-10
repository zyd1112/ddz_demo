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

    void exitRoom(Session session, long uid, long roomId, int roomType);

    void destroyRoom(long roomId, int roomType);

    void gameStart(long roomId, int roomType);

}
