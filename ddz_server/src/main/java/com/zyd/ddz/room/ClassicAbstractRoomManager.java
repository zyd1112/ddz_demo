package com.zyd.ddz.room;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import xyz.noark.core.util.RandomUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static xyz.noark.log.LogHelper.logger;

/**
 * @author zyd
 * @date 2023/4/7 17:06
 */
public class ClassicAbstractRoomManager extends AbstractRoomManager {

    private static final AtomicInteger ROOM_NUM = new AtomicInteger(0);

    private static final AtomicLong ROOM_ID = new AtomicLong(0);

    /**
     * 可以被分配的房间
     */
    private final List<Room> availableRoom = new CopyOnWriteArrayList<>();

    /**
     * roomId -> room
     */
    private final Map<Long, Room> roomMap = new ConcurrentHashMap<>();

    /**
     * uid -> player
     */
    private final Map<Long, Player> playerMap = new ConcurrentHashMap<>();



    @Override
    public int getSize() {
        return 3;
    }


    @Override
    public void onPlayerEnter(Player player) {
        long uid = player.getUid();
        if(playerMap.containsKey(uid)){
            logger.info("{} 玩家, 断线重连", uid);
            player.setAuto(false);
            return;
        }
        long roomId;
        Room room;
        if(availableRoom.isEmpty()){
            room = new Room();
            roomId = ROOM_ID.incrementAndGet();
            room.setCreateTime(System.currentTimeMillis());
            room.setId(roomId);
            room.setName("房间-" + ROOM_NUM.incrementAndGet());
            roomMap.put(roomId, room);
            availableRoom.add(room);
        }
        room = availableRoom.get(RandomUtils.nextInt(availableRoom.size()));
        roomId = room.getId();
        player.setRoomId(roomId);

        room.getPlayers().put(uid, player);
        playerMap.put(uid, player);
        logger.info("{[]:[]} 玩家进入房间 {}", player.getUid(), player.getName(), room.getName());
    }

    @Override
    public void onPlayerExit(Room room, Player player) {
        logger.info("{} 玩家 离开: {}", player.getUid(), room.getName());

        if(!room.isStart()){
            playerMap.remove(player.getUid());
        }else{
            player.setAuto(true);
        }
    }

    @Override
    public void onPlayerReady(Room room, Player player, boolean ready){

    }

    @Override
    public void onDestroy(Room room) {
        logger.warn("{}, 销毁", room.getName());
        room.setDestroy(true);
        room.setStart(false);
        roomMap.remove(room.getId());
        room.getPlayers().forEach((playerId, player) -> {
            playerMap.remove(playerId);
        });
    }

    @Override
    public void onHeart(Room room, int dt) {
        if (!room.isStart()){
            return;
        }
        //托管机器人

    }

    @Override
    public Collection<Room> getRooms() {
        return roomMap.values();
    }

    @Override
    public Collection<Room> getAvailableRooms() {
        return availableRoom;
    }

    @Override
    public Room getRoom(long roomId) {
        return roomMap.get(roomId);
    }

    @Override
    public Map<Long, Player> getPlayers() {
        return this.playerMap;
    }

}
