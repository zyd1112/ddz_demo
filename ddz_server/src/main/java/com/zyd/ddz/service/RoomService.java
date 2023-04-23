package com.zyd.ddz.service;

import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.Room;
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
    void playerReady(Session session, long uid, int roomType);

    /**
     * 玩家开始
     */
    void playerStart(Session session, long uid, int roomType);


    /**
     * 争夺地主
     * @param scramble 是否抢地主
     */
    void scramble(Session session, long uid, int roomType, boolean scramble);

    /**
     * 出牌
     */
    void sendCard(Session session, long uid, int roomType, List<Card> cards);

    /**
     * 不出
     */
    void noSend(Session session, long uid, int roomType);

    /**
     * 提示
     */
    void suggest(Session session, long uid, int roomType);

    void reqCountdown(Session session, long uid, int roomType);

    /**
     * 超时出牌
     */
    void timeoutSend(Room room);

    /**
     * 托管
     */
    void autoRobot(Session session, long uid, int roomType, boolean choose);
}
