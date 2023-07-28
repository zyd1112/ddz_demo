package com.zyd.ddz.common.message.room.request;

import com.zyd.ddz.common.message.room.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/20 14:24
 */
@Setter
@Getter
public class ReqScrambleMessage {
    /**
     * false 不抢
     * true 抢
     */
    boolean status;

    PlayerDto playerDto;
}
