package com.zyd.ddz.event;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.room.AbstractRoomEvent;
import com.zyd.ddz.service.RoomService;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class RoomHeartEvent extends GameTask {

    @Autowired
    RoomService roomService;

    @Override
    protected void doAction() {
        List<Room> roomList = roomService.getRoom();
        roomService.getAvailableRoom()
                .removeIf(room -> room.getPlayerList().size() == room.getAbstractRoomEvent().getSize());

        for (Room room : roomList) {
            AbstractRoomEvent abstractRoomEvent = room.getAbstractRoomEvent();

            List<Player> playerList = room.getPlayerList();

            if (playerList.size() == 0){
                abstractRoomEvent.onDestroy(room);
            }

            if(!room.isDestroy()){
                abstractRoomEvent.onHeart(room);
            }
        }
    }
}
