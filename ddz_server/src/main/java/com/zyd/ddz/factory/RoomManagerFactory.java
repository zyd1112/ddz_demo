package com.zyd.ddz.factory;


import com.zyd.ddz.constant.RoomType;
import com.zyd.ddz.room.ClassicAbstractRoomManager;
import com.zyd.ddz.room.AbstractRoomManager;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Component;

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
