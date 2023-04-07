package com.zyd.ddz.factory;


import com.zyd.ddz.constant.RoomType;
import com.zyd.ddz.room.ClassicRoomEvent;
import com.zyd.ddz.room.RoomEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/7 17:12
 */
public class RoomFactory {

    private static final Map<Integer, RoomEvent> ROOM_TYPE = new HashMap<Integer, RoomEvent>(){{
        put(RoomType.CLASSIC, new ClassicRoomEvent());
    }};

    public static RoomEvent getRoom(int type){
        return ROOM_TYPE.get(type);
    }
}
