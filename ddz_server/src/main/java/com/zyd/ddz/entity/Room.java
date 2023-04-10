package com.zyd.ddz.entity;

import com.zyd.ddz.room.AbstractRoomManager;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zyd
 * @date 2023/4/7 17:20
 */
@Setter
@Getter
public class Room {

    private long id;

    private String name;

    private long createTime;

    private Map<Long, Player> players = new ConcurrentHashMap<>();

    private AbstractRoomManager abstractRoomManager;

    private boolean isDestroy;

    private long gameStartTime;

    private int gameReadyTime;

    private int waitDestroyTime;

    private boolean start;

    /**
     * 玩家角色分配 id -> type 1: 地主
     *                       2-3: 农民
     *
     */
    private Map<Long, Integer> playerCharacter = new ConcurrentHashMap<>();

}
