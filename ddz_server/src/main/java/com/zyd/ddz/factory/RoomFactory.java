package com.zyd.ddz.factory;


import com.zyd.ddz.constant.RoomType;
import com.zyd.ddz.room.ClassicAbstractRoomEvent;
import com.zyd.ddz.room.AbstractRoomEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyd
 * @date 2023/4/7 17:12
 */
public class RoomFactory {

    private static final Map<Integer, AbstractRoomEvent> ROOM_TYPE = new HashMap<Integer, AbstractRoomEvent>(){{
        put(RoomType.CLASSIC, new ClassicAbstractRoomEvent());
    }};

    public static AbstractRoomEvent getRoom(int type){
        return ROOM_TYPE.get(type);
    }
}
