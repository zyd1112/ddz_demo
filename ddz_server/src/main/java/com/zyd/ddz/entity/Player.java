package com.zyd.ddz.entity;

import com.zyd.ddz.constant.CharacterType;
import lombok.Getter;
import lombok.Setter;
import xyz.noark.core.network.Session;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Player {
    long uid;
    long roomId;
    String name;

    int joyBeans;

    List<Card> cardList = new ArrayList<>();

    /**
     * 是否是托管机器人
     */
    boolean auto;

    /**
     * 是否准备
     */
    boolean ready;

    Session session;

    CharacterType character;

    long scrambleTime;

    int scrambleCount;
}
