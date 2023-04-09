package com.zyd.ddz.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Player {
    long uid;
    long roomId;

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
}
