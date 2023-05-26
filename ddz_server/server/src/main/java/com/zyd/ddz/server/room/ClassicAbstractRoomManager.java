package com.zyd.ddz.server.room;


import com.zyd.ddz.common.constant.CharacterType;
import com.zyd.ddz.common.constant.RoomType;
import com.zyd.ddz.common.entity.Player;
import com.zyd.ddz.common.entity.Room;
import com.zyd.ddz.common.message.room.response.ResGameOverRewardMessage;
import com.zyd.ddz.common.manager.AbstractRoomManager;
import com.zyd.ddz.common.utils.MessageUtils;
import com.zyd.ddz.common.utils.TimeUtils;
import com.zyd.ddz.server.service.impl.RoomServiceImpl;
import xyz.noark.core.ioc.IocHolder;

import static xyz.noark.log.LogHelper.logger;

/**
 * @author zyd
 * @date 2023/4/7 17:06
 */
public class ClassicAbstractRoomManager extends AbstractRoomManager {

    @Override
    public String getName() {
        return "欢乐斗地主 经典房";
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    public int getType() {
        return RoomType.CLASSIC;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public int getReward() {
        return 1000;
    }

    @Override
    public void onGameOver(Room room) {
        logger.info("[{}:{}] 游戏结束, 正在结算奖励", room.getId(), room.getName());
        room.setGameOver(true);
        room.setGameOverTime(TimeUtils.getNowTimeMillis());
        room.setStart(false);
        ResGameOverRewardMessage message = new ResGameOverRewardMessage();
        int value = room.getMultiple() * getReward();
        for (Player player : room.getPlayers().values()) {
            int increase;
            if(player.getCardList().size() == 0){
                increase = player.getCharacter().equals(CharacterType.LANDOWNER) ? value : value / 2;
            }else {
                increase = player.getCharacter().equals(CharacterType.LANDOWNER) ? -value : -value / 2;
            }
            if(player.isLeave()){
                increase = 0;
            }
            player.setJoyBeans(player.getJoyBeans() + increase);
            player.init();
            message.getPlayerRewards().put(player.getUid(), increase);
        }
        RoomServiceImpl roomService = IocHolder.getIoc().get(RoomServiceImpl.class);
        roomService.sendRewards(room);

        MessageUtils.sendMessageForRoom(room, message);
        logger.info("[{}:{}] 游戏结束, 结算奖励成功", room.getId(), room.getName());
    }

    @Override
    public void onHeart(Room room, int dt) {
        if (!room.isStart()){
            return;
        }
        //托管机器人
        Player nextPlayer = room.getNextPlayer();
        if(nextPlayer != null && nextPlayer.isAuto()){
            int wait = nextPlayer.getWait();
            wait += dt;
            if(wait >= 2000){
                wait = 0;
                RoomServiceImpl roomService = IocHolder.getIoc().get(RoomServiceImpl.class);
                roomService.autoSend(room, nextPlayer);
            }
            nextPlayer.setWait(wait);
        }
    }

}
