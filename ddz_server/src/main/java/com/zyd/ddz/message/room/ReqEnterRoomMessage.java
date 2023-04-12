package com.zyd.ddz.message.room;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/12 11:15
 */
@Setter
@Getter
public class ReqEnterRoomMessage {
    long uid;

    int roomType;
}
