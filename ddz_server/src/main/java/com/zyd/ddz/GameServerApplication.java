package com.zyd.ddz;

import com.zyd.ddz.constant.ExecutorType;
import com.zyd.ddz.event.RoomHeartEvent;
import com.zyd.ddz.utils.ExecutorUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Component;
import xyz.noark.game.Noark;

import java.util.concurrent.TimeUnit;

/**
 * @author zyd
 * @date 2022/12/26 12:37
 */
public class GameServerApplication {

    public static void main(String[] args) {
        Noark.run(GameServerBootstrap.class, args);

    }
}
