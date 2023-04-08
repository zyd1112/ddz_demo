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
}
