package com.zyd.ddz.common.message.room.response;

import com.zyd.ddz.common.message.Message;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/20 11:30
 */
@Getter
@Setter
public class ResRoomTimeHeartMessage implements Message {
    @Override
    public int getOpcode() {
        return 1005;
    }

    int time;
}
