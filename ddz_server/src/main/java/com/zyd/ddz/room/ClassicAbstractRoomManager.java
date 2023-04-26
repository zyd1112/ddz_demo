package com.zyd.ddz.room;

import com.zyd.ddz.constant.RoomType;
import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.service.impl.RoomServiceImpl;
import xyz.noark.core.ioc.IocHolder;



/**
 * @author zyd
 * @date 2023/4/7 17:06
 */
public class ClassicAbstractRoomManager extends AbstractRoomManager {


    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public int getType() {
        return RoomType.CLASSIC;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public int getReward() {
        return 1000;
    }

    @Override
    public void onHeart(Room room, int dt) {
        if (!room.isStart()){
            return;
        }
        //托管机器人
        Player nextPlayer = room.getNextPlayer();
        if(nextPlayer != null && nextPlayer.isAuto()){
            int wait = nextPlayer.getWait();
            wait += dt;
            if(wait >= 2000){
                wait = 0;
                RoomServiceImpl roomService = IocHolder.getIoc().get(RoomServiceImpl.class);
                roomService.autoSend(room, nextPlayer);
            }
            nextPlayer.setWait(wait);
        }
    }

}
