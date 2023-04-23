package com.zyd.ddz.message.room;

import com.zyd.ddz.message.Message;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/20 11:30
 */
@Getter
@Setter
public class ResRoomTimeHeartMessage implements Message {
    int opcode = 1005;
    int time;
}
