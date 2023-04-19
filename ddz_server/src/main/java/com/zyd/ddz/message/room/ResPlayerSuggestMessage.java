package com.zyd.ddz.message.room;

import com.zyd.ddz.entity.Card;
import com.zyd.ddz.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/18 9:29
 */
@Setter
@Getter
public class ResPlayerSuggestMessage implements Message {
    int opcode = 1003;

    List<Card> availableCards;
}
