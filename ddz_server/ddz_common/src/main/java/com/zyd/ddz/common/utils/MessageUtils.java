package com.zyd.ddz.common.utils;

import com.zyd.ddz.common.entity.Player;
import com.zyd.ddz.common.entity.Room;
import com.zyd.ddz.common.message.Message;
import xyz.noark.core.network.Session;
import xyz.noark.core.network.SessionManager;

/**
 * @author zyd
 * @date 2023/4/20 13:07
 */
public class MessageUtils {

    public static void sendMessage(long uid, Message message){
        Session session = SessionManager.getSessionByPlayerId(uid);
        if(session != null){
            session.send(message.getOpcode(), message);
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
