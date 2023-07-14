package com.zyd.ddz.common.utils;

import com.zyd.ddz.common.entity.Player;
import com.zyd.ddz.common.entity.Room;
import com.zyd.ddz.common.message.Message;
import com.zyd.zgame.common.network.Session;
import com.zyd.zgame.common.network.SessionManager;

/**
 * @author zyd
 * @date 2023/4/20 13:07
 */
public class MessageUtils {

    public static void sendMessage(long uid, Message message){
        Session session = SessionManager.getSessionByUid(uid);
        if(session != null){
            session.sendMessage(message.getOpcode(), message);
        }
    }

    public static void sendMessageForRoom(Room room, Message message){
        for (Player player : room.getPlayers().values()) {
            if(!player.isLeave()){
                sendMessage(player.getUid(), message);
            }
        }
    }
}
