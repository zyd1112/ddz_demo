package com.zyd.ddz.event;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomManagerFactory;
import com.zyd.ddz.room.AbstractRoomManager;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RoomHeartEvent extends GameTask {


    long curTime = System.currentTimeMillis();

    @Override
    protected void doAction() {
        long now = System.currentTimeMillis();
        int dt = (int) (now - curTime);
        curTime = now;
        Map<Integer, AbstractRoomManager> rooms = RoomManagerFactory.getRooms();
        rooms.forEach((type, roomManager) -> {
            Collection<Room> roomList = roomManager.getRooms();
            roomManager.getAvailableRooms()
                    .removeIf(room -> room.getPlayerList().size() == room.getAbstractRoomManager().getSize());

            for (Room room : roomList) {
                if(room.isDestroy()){
                    continue;
                }
                AbstractRoomManager abstractRoomManager = room.getAbstractRoomManager();

                List<Player> playerList = room.getPlayerList();
                int waitDestroyTime = 0;
                if (playerList.size() == 0){
                    waitDestroyTime = room.getWaitDestroyTime();
                    waitDestroyTime += dt;
                    if(waitDestroyTime >= 1000){
                        abstractRoomManager.onDestroy(room);
                    }
                }
                room.setWaitDestroyTime(waitDestroyTime);
                abstractRoomManager.onHeart(room, dt);
            }
        });

    }
}
