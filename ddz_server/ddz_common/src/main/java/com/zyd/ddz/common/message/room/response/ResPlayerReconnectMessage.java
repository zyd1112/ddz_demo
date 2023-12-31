package com.zyd.ddz.common.message.room.response;

import com.zyd.ddz.common.entity.Card;
import com.zyd.ddz.common.message.room.dto.PlayerDto;
import com.zyd.ddz.common.message.Message;
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
    @Override
    public int getOpcode() {
        return 1010;
    }

    long nextId;
    long firstId;
    int multiple;
    List<Card> garbageList = new ArrayList<>();

    List<PlayerDto> playerInfos = new ArrayList<>();

}
