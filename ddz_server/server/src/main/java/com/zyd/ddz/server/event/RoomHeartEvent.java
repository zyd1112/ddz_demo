package com.zyd.ddz.server.event;

import com.zyd.ddz.common.entity.Player;
import com.zyd.ddz.common.entity.Room;
import com.zyd.ddz.common.message.room.response.ResRoomReadyTimeMessage;
import com.zyd.ddz.common.message.room.response.ResRoomTimeHeartMessage;
import com.zyd.ddz.common.manager.AbstractRoomManager;
import com.zyd.ddz.common.utils.MessageUtils;

import com.zyd.ddz.server.service.impl.RoomServiceImpl;
import com.zyd.zgame.common.network.SessionManager;
import com.zyd.zgame.common.utils.ApplicationContextUtils;
import com.zyd.zgame.core.thread.command.AbstractScheduleCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
public class RoomHeartEvent extends AbstractScheduleCommand {


    long curTime = System.currentTimeMillis();
    AbstractRoomManager roomManager;

    public RoomHeartEvent(AbstractRoomManager roomManager){
        this.roomManager = roomManager;
        log.info("{}, 心跳事件启动", roomManager.getName());
    }

    /**
     * 准备时间
     */
    private final int readyTime = 10000;

    @Override
    public void exe() {
        long now = System.currentTimeMillis();
        int dt = (int) (now - curTime);
        curTime = now;
        Collection<Room> roomList = roomManager.getRooms();

        for (Room room : roomList) {
            if(room.isDestroy()){
                continue;
            }
            AbstractRoomManager abstractRoomManager = room.getAbstractRoomManager();

            Map<Long, Player> playerList = room.getPlayers();
            playerList.forEach((k, p) -> {
                if(!SessionManager.isOnline(p.getUid())){
                    if(!p.isLeave()){
                        abstractRoomManager.onPlayerExit(room, p);
                    }
                }

            });
            int waitDestroyTime;
            if (playerList.size() == 0 || checkAllLeave(playerList)){
                waitDestroyTime = room.getWaitDestroyTime();
                waitDestroyTime += dt;
                if(waitDestroyTime >= 10000){
                    abstractRoomManager.onDestroy(room);
                }
                room.setWaitDestroyTime(waitDestroyTime);
            }
            if(!room.isStart()){
                checkReady(room, dt);
            }else if(!room.isGameOver()){
                updateHeart(room, dt, roomManager.getTimeout());
            }
            abstractRoomManager.onHeart(room, dt);
        }

    }

    private void updateHeart(Room room, int dt, int timeoutLimit) {
        int clockCountDown = room.getClockCountDown();
        clockCountDown += dt;
        if(clockCountDown >= 1000){
            clockCountDown = 0;
            checkTimeout(room, timeoutLimit);
            ResRoomTimeHeartMessage message = new ResRoomTimeHeartMessage();
            message.setTime(room.getTimeout());
            MessageUtils.sendMessageForRoom(room, message);
        }
        room.setClockCountDown(clockCountDown);
    }

    private void checkTimeout(Room room, int timeoutLimit) {
        int timeout = room.getTimeout();
        timeout++;
        if(timeout >= timeoutLimit){
            ApplicationContextUtils.getBean(RoomServiceImpl.class).autoSend(room, room.getNextPlayer());
        }else {
            room.setTimeout(timeout);
        }
    }

    private boolean checkAllLeave(Map<Long, Player> playerList) {
        for (Player player : playerList.values()) {
            if(!player.isLeave()){
                return false;
            }
        }
        return true;
    }

    private void checkReady(Room room, int dt) {
        Collection<Player> playerList = room.getPlayers().values();
        boolean ready = false;
        int gameReadyTime = room.getGameReadyTime();
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

        ResRoomReadyTimeMessage message = new ResRoomReadyTimeMessage();
        int time = (readyTime - gameReadyTime) / 1000;
        message.setCountDown(time);
        message.setStart(ready);
        MessageUtils.sendMessageForRoom(room, message);


        if(gameReadyTime >= readyTime){
            log.info("{} 游戏开始", room.getName());
            gameReadyTime = 0;
            room.getAbstractRoomManager().onGameStart(room);
        }
        room.setGameReadyTime(gameReadyTime);

    }

    @Override
    public long getInitialDelay() {
        return 0;
    }

    @Override
    public long getDelay() {
        return 100;
    }

    @Override
    public TimeUnit getUnit() {
        return TimeUnit.MILLISECONDS;
    }

}
