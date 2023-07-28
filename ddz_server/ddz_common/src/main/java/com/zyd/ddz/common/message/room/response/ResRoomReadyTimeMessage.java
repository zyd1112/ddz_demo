package com.zyd.ddz.common.message.room.response;

import com.zyd.ddz.common.message.Message;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/21 9:38
 */
@Getter
@Setter
public class ResRoomReadyTimeMessage implements Message {
    @Override
    public int getOpcode() {
        return 1007;
    }

    /**
     * 倒计时
     */
    int countDown;

    /**
     * 是否开始
     */
    boolean start;
}
