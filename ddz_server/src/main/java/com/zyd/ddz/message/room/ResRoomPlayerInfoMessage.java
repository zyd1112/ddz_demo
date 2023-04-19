package com.zyd.ddz.message.room;

import com.zyd.ddz.message.Message;
import com.zyd.ddz.message.room.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/19 14:15
 */
@Setter
@Getter
public class ResRoomPlayerInfoMessage implements Message {
    int opcode = 1004;

    List<PlayerDto> playerInfos = new ArrayList<>();
}
