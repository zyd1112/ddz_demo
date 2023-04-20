package com.zyd.ddz.message.room;

import com.zyd.ddz.message.Message;
import com.zyd.ddz.message.room.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/20 14:24
 */
@Setter
@Getter
public class ReqScrambleMessage implements Message {
    int opcode = 16;

    /**
     * false 不抢
     * true 抢
     */
    boolean status;

    PlayerDto playerDto;

}
