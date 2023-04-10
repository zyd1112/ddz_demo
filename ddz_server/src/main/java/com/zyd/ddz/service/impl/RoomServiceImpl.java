package com.zyd.ddz.service.impl;

import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.entity.Player;
import com.zyd.ddz.entity.Room;
import com.zyd.ddz.factory.RoomManagerFactory;
import com.zyd.ddz.room.AbstractRoomManager;
import com.zyd.ddz.service.RoomService;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;

/**
 * @author zyd
 * @date 2023/4/7 17:00
 */
@Service
public class RoomServiceImpl implements RoomService {


    @Autowired
    UserDao userDao;
    @Override
    public boolean enterRoom(Session session, long uid, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return false;
        }
        UserDomain userDomain = userDao.cacheGet(uid);
        if(userDomain == null){
            return false;
        }
        Player player = new Player();
        player.setJoyBeans(userDomain.getJoyBeans());
        player.setUid(uid);
        player.setSession(session);
        roomManager.onPlayerEnter(player);
        return true;
    }


    @Override
    public void exitRoom(Session session, long uid, long roomId, int roomType) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        Room room = roomManager.getRoom(roomId);
        if(room == null){
            return;
        }
        roomManager.onPlayerExit(room, uid);
    }

    @Override
    public void gameReady(Session session, long uid, long roomId, int roomType, boolean ready) {
        AbstractRoomManager roomManager = RoomManagerFactory.getRoom(roomType);
        if(roomManager == null){
            return;
        }
        Room room = roomManager.getRoom(roomId);
        if(room == null){
            return;
        }
        roomManager.onPlayerReady(room, uid, ready);
    }

}
