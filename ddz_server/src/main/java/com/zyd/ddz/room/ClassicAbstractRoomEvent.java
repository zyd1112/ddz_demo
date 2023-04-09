package com.zyd.ddz.room;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.service.DdzService;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Component;

import static xyz.noark.log.LogHelper.logger;

/**
 * @author zyd
 * @date 2023/4/7 17:06
 */
@Component
public class ClassicAbstractRoomEvent extends AbstractRoomEvent {

    @Autowired
    DdzService ddzService;

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
    public void onPlayerExit(Room room, Player player) {

    }

    public void onGameStart(Room room){}

    @Override
    public void onHeart(Room room) {

    }
}
