package com.zyd.ddz.service;

import xyz.noark.core.network.Session;

/**
 * @author zyd
 * @date 2023/4/7 16:59
 */
public interface RoomService {

    /**
     * 玩家进入房间
     */
    boolean enterRoom(Session session, long uid, int roomType);

    /**
     * 玩家离开房间
     */
    void exitRoom(Session session, long uid, long roomId, int roomType);

    /**
     * 玩家准备
     */
    void playerReady(Session session, long uid, long roomId, int roomType, boolean ready);


    /**
     * 争夺地主
     * @param type 1: 叫地主, 2: 抢地主
     */
    void scramble(Session session, long uid, int roomType, long roomId, int type);
}
