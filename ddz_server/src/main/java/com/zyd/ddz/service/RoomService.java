package com.zyd.ddz.service;

import com.zyd.ddz.entity.Card;
import xyz.noark.core.network.Session;

import java.util.List;

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
    void exitRoom(Session session, long uid, int roomType);

    /**
     * 玩家准备
     */
    void playerReady(Session session, long uid, int roomType, boolean ready);


    /**
     * 争夺地主
     * @param scramble 是否抢地主
     */
    void scramble(Session session, long uid, int roomType, boolean scramble);

    /**
     * 出牌
     */
    void sendCard(Session session, long uid, int roomType, List<Card> cards);
}
