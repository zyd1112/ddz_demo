package com.zyd.ddz.room;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.service.DdzService;
import com.zyd.ddz.utils.IdManager;
import com.zyd.ddz.utils.RandomUtils;
import lombok.Setter;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Component;

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
     * roleId -> roomId
     */
    private final Map<Long, Long> playerToRoomMap = new ConcurrentHashMap<>();

    private final Map<Long, Player> playerMap = new ConcurrentHashMap<>();


    @Override
    public int getSize() {
        return 3;
    }


    @Override
    public void onPlayerEnter(Player player) {
        long uid = player.getUid();

        Long roomId = playerToRoomMap.get(uid);
        Room room;
        if(availableRoom.isEmpty()){
            room = new Room();
            roomId = ROOM_ID.incrementAndGet();
            playerToRoomMap.put(uid, roomId);
            room.setCreateTime(System.currentTimeMillis());
            room.setId(roomId);
            room.setName("房间-" + ROOM_NUM.incrementAndGet());
            roomMap.put(roomId, room);
            availableRoom.add(room);
        }
        room = availableRoom.get(RandomUtils.getRandomNum(availableRoom.size()));

        player.setRoomId(roomId);

        room.getPlayerList().add(player);
        playerMap.put(uid, player);
    }

    @Override
    public void onPlayerExit(Room room, long uid) {
        if (!this.playerToRoomMap.containsKey(uid)) {
            return;
        }
        List<Player> playerList = room.getPlayerList();
        Player player = null;
        int index = 0;
        for (Player p : playerList) {
            if(p.getUid() == uid){
                player = p;
                break;
            }
            index++;
        }
        if(player == null) {
            return;
        }

        logger.info("{} 玩家 离开: {}", uid, room.getName());

        if(room.getGameStartTime() > 0){
            playerList.remove(index);
        }else{
            player.setAuto(true);
        }
    }

    @Override
    public void onGameStart(Room room){}

    @Override
    public void onDestroy(Room room) {
        logger.warn("{}, 销毁", room.getName());
        room.setDestroy(true);
    }

    @Override
    public void onHeart(Room room, int dt) {

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

}
