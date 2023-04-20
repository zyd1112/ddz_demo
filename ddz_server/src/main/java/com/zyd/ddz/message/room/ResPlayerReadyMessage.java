package com.zyd.ddz.message.room;

import com.zyd.ddz.message.Message;
import com.zyd.ddz.message.room.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyd
 * @date 2023/4/20 20:18
 */
@Setter
@Getter
public class ResPlayerReadyMessage implements Message {
    int opcode = 1006;

    List<PlayerDto> playerDtoList = new ArrayList<>();
}
