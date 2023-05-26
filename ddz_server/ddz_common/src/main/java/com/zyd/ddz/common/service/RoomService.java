package com.zyd.ddz.common.service;


import com.zyd.ddz.common.entity.Card;
import com.zyd.ddz.common.entity.Player;
import com.zyd.ddz.common.entity.Room;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/7 16:59
 */
public interface RoomService {

    /**
     * 玩家进入房间
     */
    boolean enterRoom(long uid, int roomType);

    /**
     * 玩家离开房间
     */
    void exitRoom(long uid, int roomType);

    /**
     * 玩家准备
     */
    void playerReady(long uid, int roomType);

    /**
     * 玩家开始
     */
    void playerStart(long uid, int roomType);


    /**
     * 争夺地主
     * @param scramble 是否抢地主
     */
    void scramble(long uid, int roomType, boolean scramble);

    /**
     * 出牌
     */
    void sendCard(long uid, int roomType, List<Card> cards);

    /**
     * 不出
     */
    void noSend(long uid, int roomType);

    /**
     * 提示
     */
    void suggest(long uid, int roomType);

    void reqCountdown(long uid, int roomType);

    /**
     * 自动出牌
     */
    void autoSend(Room room, Player player);

    /**
     * 结算奖励
     */
    void sendRewards(Room room);

    /**
     * 玩家离开
     */
    void playerLeave(long uid, int roomType);

    /**
     * 托管
     */
    void autoRobot(long uid, int roomType, boolean choose);
}
