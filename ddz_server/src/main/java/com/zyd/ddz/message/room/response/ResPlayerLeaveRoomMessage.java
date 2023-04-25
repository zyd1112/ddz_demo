package com.zyd.ddz.message.room.response;

import com.zyd.ddz.message.Message;
import com.zyd.ddz.message.room.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyd
 * @date 2023/4/21 10:58
 */
@Setter
@Getter
public class ResPlayerLeaveRoomMessage implements Message {
    int opcode = 1008;

    long uid;
    List<PlayerDto> playerList = new ArrayList<>();
}