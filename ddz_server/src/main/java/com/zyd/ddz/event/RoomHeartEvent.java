package com.zyd.ddz.event;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomManagerFactory;
import com.zyd.ddz.room.AbstractRoomManager;
import com.zyd.ddz.utils.TimeUtils;
import xyz.noark.core.annotation.Component;
import xyz.noark.core.network.SessionManager;
import xyz.noark.game.monitor.AbstractMonitorService;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static xyz.noark.log.LogHelper.logger;


@Component
public class RoomHeartEvent extends AbstractMonitorService {


    long curTime = System.currentTimeMillis();

    /**
     * 准备时间
     */
    private final int readyTime = 1000;

    public void doAction() {
        long now = System.currentTimeMillis();
        int dt = (int) (now - curTime);
        curTime = now;
        Map<Integer, AbstractRoomManager> rooms = RoomManagerFactory.getRooms();
        rooms.forEach((type, roomManager) -> {
            Collection<Room> roomList = roomManager.getRooms();

            for (Room room : roomList) {
                if(room.isDestroy()){
                    continue;
                }
                AbstractRoomManager abstractRoomManager = room.getAbstractRoomManager();

                Map<Long, Player> playerList = room.getPlayers();
                playerList.forEach((k, p) -> {
                    if(SessionManager.getSession(p.getSession().getId()) == null){
                        if(!p.isLeave()){
                            abstractRoomManager.onPlayerExit(room, p);
                        }
                        p.setLeave(true);
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
                }
                abstractRoomManager.onHeart(room, dt);
            }
        });

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
            room.getAbstractRoomManager().onGameStart(room);
        }
        room.setGameStartTime(gameStartTime);
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

    @Override
    protected void exe() {
        doAction();
    }
}
