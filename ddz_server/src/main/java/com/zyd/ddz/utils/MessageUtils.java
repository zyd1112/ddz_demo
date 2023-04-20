package com.zyd.ddz.utils;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.message.Message;
import xyz.noark.core.network.Session;

/**
 * @author zyd
 * @date 2023/4/20 13:07
 */
public class MessageUtils {

    public static void sendMessage(Player player, Message message){
        player.getSession().send(message.getOpcode(), message);
    }

    public static void sendMessage(Session session, Message message){
        session.send(message.getOpcode(), message);
    }

    public static void sendMessageForRoom(Room room, Message message){
        for (Player player : room.getPlayers().values()) {
            sendMessage(player, message);
        }
    }
}
