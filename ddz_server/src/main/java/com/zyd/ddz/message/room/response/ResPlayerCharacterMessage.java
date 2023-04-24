package com.zyd.ddz.message.room.response;

import com.zyd.ddz.entity.Card;
import com.zyd.ddz.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/23 14:53
 */
@Setter
@Getter
public class ResPlayerCharacterMessage implements Message {
    int opcode = 1000;

    Map<Long, List<Card>> cardsMap = new HashMap<>();

    Map<Long, Integer> character = new HashMap<>();

    int multiple;

    long uid;

    boolean status;

    boolean success;

}
