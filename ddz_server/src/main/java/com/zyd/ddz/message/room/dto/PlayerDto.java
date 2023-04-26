package com.zyd.ddz.message.room.dto;

import com.zyd.ddz.entity.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyd
 * @date 2023/4/18 9:34
 */
@Setter
@Getter
public class PlayerDto {
    long uid;

    String name;

    int roomType;

    int characterType;

    boolean roomHost;

    boolean ready;

    long enterTime;

    int imageIndex;

    int joyBeans;

    boolean auto;

    List<Card> cards = new ArrayList<>();

}
