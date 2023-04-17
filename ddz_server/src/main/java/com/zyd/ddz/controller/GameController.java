package com.zyd.ddz.controller;

import com.zyd.ddz.message.login.dto.UserDto;
import com.zyd.ddz.message.room.ReqEnterRoomMessage;
import com.zyd.ddz.message.room.ReqSendCardsMessage;
import com.zyd.ddz.service.RoomService;
import com.zyd.ddz.service.UserService;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Controller;
import xyz.noark.core.annotation.controller.ExecThreadGroup;
import xyz.noark.core.annotation.controller.PacketMapping;
import xyz.noark.core.network.Session;
import xyz.noark.core.network.SessionManager;

import static xyz.noark.log.LogHelper.logger;

/**
 * @author zyd
 * @date 2023/4/12 10:39
 */
@Controller(threadGroup = ExecThreadGroup.ModuleThreadGroup)
public class GameController {

    @Autowired
    RoomService roomService;
    @Autowired
    UserService userService;

    @PacketMapping(opcode = 10, state = Session.State.CONNECTED)
    public void enterRoom(ReqEnterRoomMessage message){
        long uid = message.getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }
        Session session = SessionManager.getSessionByPlayerId(uid);
        UserDto userDto = userService.getById(session, uid);
        if(userDto == null){
            logger.info("用户不存在");
            return;
        }
        roomService.enterRoom(session, uid, message.getRoomType());
    }

    @PacketMapping(opcode = 12, state = Session.State.CONNECTED)
    public void sendCard(ReqSendCardsMessage message){
        long uid = message.getUid();
        if (!SessionManager.isOnline(uid)){
            logger.info("[{}] 用户没有在线", uid);
            return;
        }
        Session session = SessionManager.getSessionByPlayerId(uid);
        UserDto userDto = userService.getById(session, uid);
        if(userDto == null){
            logger.info("用户不存在");
            return;
        }
        roomService.sendCard(session, message.getUid(), message.getRoomType(), message.getCardList());
    }
}
