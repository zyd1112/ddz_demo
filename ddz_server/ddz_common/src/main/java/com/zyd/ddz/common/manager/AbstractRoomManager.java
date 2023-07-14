package com.zyd.ddz.common.manager;

import com.zyd.ddz.common.entity.Card;
import com.zyd.ddz.common.entity.Player;
import com.zyd.ddz.common.entity.Room;
import com.zyd.ddz.common.message.room.dto.PlayerDto;
import com.zyd.ddz.common.message.room.response.*;
import com.zyd.ddz.common.utils.GameLogicUtils;
import com.zyd.ddz.common.utils.MessageUtils;
import com.zyd.zgame.common.utils.RandomUtils;
import com.zyd.zgame.common.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author zyd
 * @date 2023/4/7 17:03
 * 定义房间事件的抽象类
 *
 */
@Slf4j
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

    public abstract int getTimeout();

    public abstract int getReward();

    public abstract String getName();

    /**
     * 创建房间事件
     */
    public void onCreate(Room room){}

    /**
     * 玩家加入房间事件
     */
    public void onPlayerEnter(Player player){
        long uid = player.getUid();
        if(playerMap.containsKey(uid)){
            log.info("{} 玩家, 断线重连", uid);
            player.setAuto(false);
            player.setLeave(false);
            ResPlayerReconnectMessage message = new ResPlayerReconnectMessage();
            Room room = getRoom(player.getRoomId());
            room.getPlayers().forEach((id, p) -> {
                PlayerDto playerDto = p.packPlayerDto();
                playerDto.setRoomType(getType());
                message.getPlayerInfos().add(playerDto);
            });
            Player curPlayer = room.getCurPlayer();
            message.setMultiple(room.getMultiple());
            message.setFirstId(curPlayer == null ? 0 : curPlayer.getUid());
            message.setNextId(room.getNextPlayer().getUid());
            message.getGarbageList().addAll(curPlayer == null ? Collections.emptyList() : curPlayer.getSendCard());
            MessageUtils.sendMessageForRoom(room, message);
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
                int index = RandomUtils.random(0, availableRoom.size());
                Room temp = availableRoom.get(index);
                if(temp.isDestroy() || temp.getPlayers().size() >= getSize()){
                    availableRoom.remove(index);
                }else {
                    room = temp;
                }
            }
        }
        roomId = room.getId();
        player.setEnterTime(TimeUtils.getNowTimeMillis());
        player.setRoomId(roomId);
        player.setRoomHost(room.getPlayers().isEmpty());
        player.setReady(player.isRoomHost());
        room.getPlayers().put(uid, player);
        playerMap.put(uid, player);

        log.info("[{}:{}] 玩家进入房间 {}", player.getUid(), player.getName(), room.getName());

        ResPlayerEnterRoomMessage message = new ResPlayerEnterRoomMessage();
        room.getPlayers().forEach((id, p) -> {
            PlayerDto playerDto = p.packPlayerDto();
            playerDto.setRoomType(getType());
            message.getPlayerInfos().add(playerDto);
        });

        MessageUtils.sendMessageForRoom(room, message);
    }

    /**
     * 玩家离开房间事件
     */
    public void onPlayerExit(Room room, Player player){
        log.info("{} 玩家 离开: {}", player.getUid(), room.getName());
        player.setLeave(true);
        if(!room.isStart() || room.isGameOver()){
            playerMap.remove(player.getUid());
            room.getPlayers().remove(player.getUid());
            if(player.isRoomHost()){
                for (Player value : playerMap.values()) {
                    value.setRoomHost(true);
                    value.setReady(true);
                    break;
                }
            }
            room.setCurPlayer(null);
        }else{
            player.setAuto(true);
        }

        ResPlayerLeaveRoomMessage message = new ResPlayerLeaveRoomMessage();
        room.getPlayers().forEach((id, p) -> {
            PlayerDto playerDto = p.packPlayerDto();
            message.getPlayerMap().put(id, playerDto);
        });
        MessageUtils.sendMessageForRoom(room, message);
    }

    /**
     * 房间销毁事件
     */
    public void onDestroy(Room room){
        log.warn("{}, 销毁", room.getName());
        room.setDestroy(true);
        room.setStart(false);
        roomMap.remove(room.getId());
        room.getPlayers().forEach((playerId, player) -> playerMap.remove(playerId));
    }

    /**
     * 玩家准备事件
     */
    public void onPlayerReady(Room room, Player player, boolean ready){}

    /**
     * 游戏开始事件
     */
    public void onGameStart(Room room){
        room.setStart(true);
        room.setGameStartTime(TimeUtils.getNowTimeMillis());
        room.setGameOver(false);
        Map<Long, Player> players = room.getPlayers();
        List<List<Card>> cards = GameLogicUtils.sendCard();
        ResPlayerCardMessage message = new ResPlayerCardMessage();
        List<Player> playerList = players.values().stream().sorted(Comparator.comparing(Player::getCharacter))
                .collect(Collectors.toList());
        room.getDownCards().addAll(cards.get(3));
        for (Player player : playerList) {
            if (player.isRoomHost()){
                message.setUid(player.getUid());
                message.setFirstId(player.getUid());
                room.setNextPlayer(player);
                break;
            }
        }
        message.getDownCards().addAll(room.getDownCards());
        message.setType(0);
        players.forEach((id, p) -> {
            p.setCardList(cards.remove(0));
            message.getCardsMap().put(p.getUid(), p.getCardList());
        });
        MessageUtils.sendMessageForRoom(room, message);
    }

    public void onGameOver(Room room){

    }

    /**
     * 房间心跳事件
     */
    public void onHeart(Room room, int dt){}

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
