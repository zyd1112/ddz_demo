package com.zyd.ddz.room;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;

import java.util.concurrent.atomic.AtomicInteger;

import static xyz.noark.log.LogHelper.logger;

/**
 * @author zyd
 * @date 2023/4/7 17:06
 */

public class ClassicRoomEvent implements RoomEvent {

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public void onCreate(Room room) {
        logger.info( "{}: 创建", room.getName());
    }

    @Override
    public void onPlayerEnter(Room room, Player player) {

    }

    @Override
    public void onDestroy(Room room) {

    }

    @Override
    public void onHeart(Room room) {

    }
}
