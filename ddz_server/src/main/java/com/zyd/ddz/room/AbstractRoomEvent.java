package com.zyd.ddz.room;

import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;

/**
 * @author zyd
 * @date 2023/4/7 17:03
 * 定义房间事件的抽象类
 *
 */
public abstract class AbstractRoomEvent {
    /**
     * 大小
     * @return 房间最大承载人数
     */
    public abstract int getSize();

    /**
     * 创建房间事件
     */
    public void onCreate(Room room){};

    /**
     * 玩家加入房间事件
     */
    public void onPlayerEnter(Room room, Player player){};

    /**
     * 玩家离开房间事件
     */
    public void onPlayerExit(Room room, Player player){};

    /**
     * 房间销毁事件
     */
    public void onDestroy(Room room){};

    /**
     * 房间心跳事件
     */
    public void onHeart(Room room){};
}
