package com.zyd.ddz.message.room;

import com.zyd.ddz.entity.Card;
import com.zyd.ddz.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/14 17:13
 */
@Setter
@Getter
public class ResPlayerCardMessage implements Message {
    int opcode = 1001;
    Map<Integer, List<Card>> cardsMap = new HashMap<>();

    /**
     * 0: 发牌
     * 1: 出牌后
     */
    int type;

    List<Card> removeCards = new ArrayList<>();
}