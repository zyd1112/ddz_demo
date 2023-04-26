package com.zyd.ddz.message.room.response;

import com.zyd.ddz.message.Message;
import com.zyd.ddz.message.room.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/21 10:58
 */
@Setter
@Getter
public class ResPlayerLeaveRoomMessage implements Message {
    int opcode = 1008;

    Map<Long, PlayerDto> playerMap = new HashMap<>();
}
