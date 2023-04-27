package com.zyd.ddz.event;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.event.monitor.GameTask;
import com.zyd.ddz.message.room.response.ResRoomReadyTimeMessage;
import com.zyd.ddz.message.room.response.ResRoomTimeHeartMessage;
import com.zyd.ddz.room.AbstractRoomManager;
import com.zyd.ddz.service.impl.RoomServiceImpl;
import com.zyd.ddz.utils.MessageUtils;
import xyz.noark.core.ioc.IocHolder;
import xyz.noark.core.network.SessionManager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static xyz.noark.log.LogHelper.logger;


public class RoomHeartEvent extends GameTask {


    long curTime = System.currentTimeMillis();
    AbstractRoomManager roomManager;

    public RoomHeartEvent(AbstractRoomManager roomManager){
        this.roomManager = roomManager;
        logger.info("{}, 心跳事件启动", roomManager.getName());
    }

    /**
     * 准备时间
     */
    private final int readyTime = 10000;

    public void doAction() {
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
            IocHolder.getIoc().get(RoomServiceImpl.class).autoSend(room, room.getNextPlayer());
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
            logger.info("{} 游戏开始", room.getName());
            gameReadyTime = 0;
            room.getAbstractRoomManager().onGameStart(room);
        }
        room.setGameReadyTime(gameReadyTime);

    }

    @Override
    protected long getInitialDelay() {
        return 0;
    }

    @Override
    protected long getDelay() {
        return 100;
    }

    @Override
    protected TimeUnit getUnit() {
        return TimeUnit.MILLISECONDS;
    }

}
