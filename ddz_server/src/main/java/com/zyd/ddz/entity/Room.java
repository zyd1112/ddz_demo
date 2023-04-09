package com.zyd.ddz.entity;

import com.zyd.ddz.room.AbstractRoomEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private List<Player> playerList = new CopyOnWriteArrayList<>();

    private AbstractRoomEvent abstractRoomEvent;

    private boolean isDestroy;

    private long gameStartTime;


}
