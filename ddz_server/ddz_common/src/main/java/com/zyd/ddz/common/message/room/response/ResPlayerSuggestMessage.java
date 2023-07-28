package com.zyd.ddz.common.message.room.response;

import com.zyd.ddz.common.entity.Card;
import com.zyd.ddz.common.message.Message;
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
    @Override
    public int getOpcode() {
        return 1003;
    }

    List<Card> availableCards;
}
