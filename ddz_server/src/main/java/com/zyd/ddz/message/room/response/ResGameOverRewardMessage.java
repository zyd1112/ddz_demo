package com.zyd.ddz.message.room.response;

import com.zyd.ddz.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/25 15:19
 */
@Setter
@Getter
public class ResGameOverRewardMessage implements Message {
    int opcode = 1009;

    /**
     * 玩家奖励 uid -> joyBeans
     */
    Map<Long, Integer> playerRewards = new HashMap<>();
}
