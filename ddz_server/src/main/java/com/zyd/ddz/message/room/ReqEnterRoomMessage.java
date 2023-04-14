package com.zyd.ddz.message.room;

import com.zyd.ddz.message.Message;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/12 11:15
 */
@Setter
@Getter
public class ReqEnterRoomMessage implements Message {
    int opcode = 10;

    long uid;

    int roomType;
}
