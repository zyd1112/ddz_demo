package com.zyd.ddz.room;

import com.zyd.ddz.entity.Card;
import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.message.room.ResPlayerCardMessage;
import com.zyd.ddz.message.room.ResRoomPlayerInfoMessage;
import com.zyd.ddz.message.room.dto.PlayerDto;
import com.zyd.ddz.utils.GameLogicUtils;
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
 * @date 2023/4/7 17:03
 * 定义房间事件的抽象类
 *
 */
public abstract class AbstractRoomManager {
    protected static final AtomicInteger ROOM_NUM = new AtomicInteger(0);

    protected static final AtomicLong ROOM_ID = new AtomicLong(0);

    /**
     * 可以被分配的房间
     */
    protected final List<Room> availableRoom = new CopyOnWriteArrayList<>();

    /**
     * roomId -> room
     */
    protected final Map<Long, Room> roomMap = new ConcurrentHashMap<>();

    /**
     * uid -> player
     */
    protected final Map<Long, Player> playerMap = new ConcurrentHashMap<>();
    /**
     * 大小
     * @return 房间最大承载人数
     */
    public abstract int getSize();

    public abstract int getType();

    /**
     * 创建房间事件
     */
    public void onCreate(Room room){};

    /**
     * 玩家加入房间事件
     */
    public void onPlayerEnter(Player player){
        long uid = player.getUid();
        if(playerMap.containsKey(uid)){
            logger.info("{} 玩家, 断线重连", uid);
            player.setAuto(false);
            return;
        }
        long roomId;
        Room room = null;
        while(room == null){
            if(availableRoom.isEmpty()){
                room = new Room();
                room.setType(getType());
                roomId = ROOM_ID.incrementAndGet();
                room.setCreateTime(System.currentTimeMillis());
                room.setId(roomId);
                room.setName("房间-" + ROOM_NUM.incrementAndGet());
                room.setAbstractRoomManager(this);
                roomMap.put(roomId, room);
                availableRoom.add(room);
            }else {
                int index = RandomUtils.nextInt(availableRoom.size());
                Room temp = availableRoom.get(index);
                if(temp.isDestroy() || temp.getPlayers().size() >= getSize()){
                    availableRoom.remove(index);
                }else {
                    room = temp;
                }
            }
        }
        roomId = room.getId();
        player.setRoomId(roomId);
        player.setCharacter(room.getCharacterTypeList().remove(0));
        room.getPlayers().put(uid, player);
        playerMap.put(uid, player);

        logger.info("[{}:{}] 玩家进入房间 {}", player.getUid(), player.getName(), room.getName());

        ResRoomPlayerInfoMessage message = new ResRoomPlayerInfoMessage();
        room.getPlayers().forEach((id, p) -> {
            PlayerDto playerDto = new PlayerDto();
            playerDto.setRoomType(getType());
            playerDto.setUid(p.getUid());
            playerDto.setCharacterType(p.getCharacter().getType());
            message.getPlayerInfos().add(playerDto);
        });
        room.getPlayers().forEach((id, p) -> p.getSession().send(message.getOpcode(), message));
    };

    /**
     * 玩家离开房间事件
     */
    public void onPlayerExit(Room room, Player player){
        logger.info("{} 玩家 离开: {}", player.getUid(), room.getName());

        if(!room.isStart()){
            playerMap.remove(player.getUid());
            room.getPlayers().remove(player.getUid());
            room.setCurPlayer(null);
        }else{
            player.setAuto(true);
        }
    };

    /**
     * 房间销毁事件
     */
    public void onDestroy(Room room){
        logger.warn("{}, 销毁", room.getName());
        room.setDestroy(true);
        room.setStart(false);
        roomMap.remove(room.getId());
        room.getPlayers().forEach((playerId, player) -> {
            playerMap.remove(playerId);
        });
    };

    /**
     * 玩家准备事件
     */
    public void onPlayerReady(Room room, Player player, boolean ready){};

    /**
     * 游戏开始事件
     */
    public void onGameStart(Room room){
        Map<Long, Player> players = room.getPlayers();
        Map<Integer, List<Card>> cards = GameLogicUtils.sendCard();
        ResPlayerCardMessage message = new ResPlayerCardMessage();
        message.setType(0);
        message.setCardsMap(cards);
        players.forEach((id, p) -> {
            p.setCardList(cards.get(p.getCharacter().ordinal() + 1));
            p.getSession().send(message.getOpcode(), message);
        });
    }

    /**
     * 房间心跳事件
     */
    public void onHeart(Room room, int dt){};

    public Collection<Room> getRooms(){
        return roomMap.values();
    }

    public Collection<Room> getAvailableRooms() {
        return availableRoom;
    }


    public Room getRoom(long roomId) {
        return roomMap.get(roomId);
    }

    public Map<Long, Player> getPlayers() {
        return this.playerMap;
    }

}
