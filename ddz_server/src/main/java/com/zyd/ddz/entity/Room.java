package com.zyd.ddz.entity;

import com.zyd.ddz.constant.CharacterType;
import com.zyd.ddz.room.AbstractRoomManager;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
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

    private int type;

    private long createTime;

    private Map<Long, Player> players = new ConcurrentHashMap<>();

    private AbstractRoomManager abstractRoomManager;

    private boolean isDestroy;

    private long gameStartTime;

    private int gameReadyTime;

    private int waitDestroyTime;

    private boolean start;

    int multiple = 15;

    private List<CharacterType> characterTypeList = new ArrayList<CharacterType>(){{
        add(CharacterType.FARMER);
        add(CharacterType.PARTNER);
        add(CharacterType.LANDOWNER);
    }};

    /**
     * 底牌
     */
    private List<Card> downCards = new ArrayList<>();
    /**
     * 当前出牌玩家
     */
    private Player curPlayer;

    /**
     * 下一个出牌玩家
     */
    private Player nextPlayer;

    private boolean gameOver;

    private long gameOverTime;


}
