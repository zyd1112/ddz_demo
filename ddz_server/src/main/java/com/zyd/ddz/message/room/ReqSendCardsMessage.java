package com.zyd.ddz.message.room;

import com.zyd.ddz.entity.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/17 9:35
 */
@Setter
@Getter
public class ReqSendCardsMessage {
    private long uid;
    private int roomType;

    private List<Card> cardList;
}
