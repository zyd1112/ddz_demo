package com.zyd.ddz.message.room.response;

import com.zyd.ddz.entity.Card;
import com.zyd.ddz.message.Message;
import com.zyd.ddz.message.room.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zyd
 * @date 2023/4/26 14:20
 */
@Setter
@Getter
public class ResPlayerReconnectMessage implements Message {
    int opcode = 1010;

    long nextId;
    long firstId;
    int multiple;
    List<Card> garbageList = new ArrayList<>();

    List<PlayerDto> playerInfos = new ArrayList<>();

}
