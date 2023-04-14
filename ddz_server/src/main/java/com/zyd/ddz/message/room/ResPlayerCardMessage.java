package com.zyd.ddz.message.room;

import com.zyd.ddz.entity.Card;
import com.zyd.ddz.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyd
 * @date 2023/4/14 17:13
 */
@Setter
@Getter
public class ResPlayerCardMessage implements Message {
    int opcode = 11;
    List<Card> cardList = new ArrayList<>();
}
