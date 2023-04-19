package com.zyd.ddz.message.room;

import com.zyd.ddz.message.Message;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/19 14:15
 */
@Setter
@Getter
public class ResPlayerCharacterMessage implements Message {
    int opcode = 1004;

    long uid;

    int characterType;
}
