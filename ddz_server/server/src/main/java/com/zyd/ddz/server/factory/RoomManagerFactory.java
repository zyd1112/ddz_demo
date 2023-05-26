package com.zyd.ddz.server.factory;


import com.zyd.ddz.common.constant.RoomType;
import com.zyd.ddz.common.manager.AbstractRoomManager;
import com.zyd.ddz.server.room.ClassicAbstractRoomManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/7 17:12
 */

public class RoomManagerFactory {


    private static final Map<Integer, AbstractRoomManager> ROOM_TYPE = new HashMap<Integer, AbstractRoomManager>(){{
        put(RoomType.CLASSIC, new ClassicAbstractRoomManager());
    }};


    public static AbstractRoomManager getRoom(int type){
        return ROOM_TYPE.get(type);
    }

    public static Map<Integer, AbstractRoomManager> getRooms(){
        return ROOM_TYPE;
    }
}
