package com.zyd.ddz.room;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;

/**
 * @author zyd
 * @date 2023/4/7 17:03
 * 定义房间事件的接口
 *
 */
public interface RoomEvent {
    /**
     * 大小
     * @return 房间最大承载人数
     */
    int getSize();

    /**
     * 创建房间事件
     */
    void onCreate(Room room);

    /**
     * 玩家加入房间事件
     */
    void onPlayerEnter(Room room, Player player);

    /**
     * 房间销毁事件
     */
    void onDestroy(Room room);

    /**
     * 房间心跳事件
     */
    void onHeart(Room room);
}
