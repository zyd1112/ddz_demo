package com.zyd.ddz.service;

import xyz.noark.core.network.Session;

/**
 * @author zyd
 * @date 2023/4/7 16:59
 */
public interface RoomService {

    boolean enterRoom(Session session, long uid, int roomType);

    void exitRoom(Session session, long uid, long roomId, int roomType);

    void gameReady(Session session, long uid, long roomId, int roomType, boolean ready);

}
