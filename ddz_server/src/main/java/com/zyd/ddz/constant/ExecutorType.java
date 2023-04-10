package com.zyd.ddz.constant;

import lombok.Getter;

@Getter
public enum ExecutorType {

    ROOM(1, "房间处理器"),

    PLAYER(2, "玩家处理器");

    int type;
    String name;

    ExecutorType(int type, String name){
        this.name = name;
        this.type = type;
    }
}
