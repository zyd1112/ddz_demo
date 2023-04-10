package com.zyd.ddz.event;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomManagerFactory;
import com.zyd.ddz.room.AbstractRoomManager;
import com.zyd.ddz.utils.TimeUtils;

import java.util.Collection;
import java.util.Map;

import static xyz.noark.log.LogHelper.logger;

public class RoomHeartEvent extends GameTask {


    long curTime = System.currentTimeMillis();

    /**
     * 准备时间
     */
    private final int readyTime = 1000;

    @Override
    protected void doAction() {
        long now = System.currentTimeMillis();
        int dt = (int) (now - curTime);
        curTime = now;
        Map<Integer, AbstractRoomManager> rooms = RoomManagerFactory.getRooms();
        rooms.forEach((type, roomManager) -> {
            Collection<Room> roomList = roomManager.getRooms();
            roomManager.getAvailableRooms()
                    .removeIf(room -> room.getPlayers().size() == room.getAbstractRoomManager().getSize());

            for (Room room : roomList) {
                if(room.isDestroy()){
                    continue;
                }
                AbstractRoomManager abstractRoomManager = room.getAbstractRoomManager();

                Map<Long, Player> playerList = room.getPlayers();
                int waitDestroyTime = 0;
                if (playerList.size() == 0){
                    waitDestroyTime = room.getWaitDestroyTime();
                    waitDestroyTime += dt;
                    if(waitDestroyTime >= 1000){
                        abstractRoomManager.onDestroy(room);
                    }
                }
                room.setWaitDestroyTime(waitDestroyTime);
                if(!room.isStart()){
                    checkReady(room, dt);
                }
                abstractRoomManager.onHeart(room, dt);
            }
        });

    }

    private void checkReady(Room room, int dt) {
        Collection<Player> playerList = room.getPlayers().values();
        boolean ready = false;
        int gameReadyTime = room.getGameReadyTime();
        long gameStartTime = room.getGameStartTime();
        if(playerList.size() == room.getAbstractRoomManager().getSize()){
            ready = true;
            for (Player player : playerList) {
                if(!player.isReady()){
                    ready = false;
                    break;
                }
            }
        }
        gameReadyTime = ready ? gameReadyTime + dt : 0;
        if(gameReadyTime >= readyTime){
            gameStartTime = TimeUtils.getNowTimeMillis();
            logger.info("{} 游戏开始", room.getName());
            room.setStart(true);
            gameReadyTime = 0;
        }
        room.setGameStartTime(gameStartTime);
        room.setGameReadyTime(gameReadyTime);

    }
}
