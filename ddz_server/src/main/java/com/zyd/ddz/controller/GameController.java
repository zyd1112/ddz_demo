package com.zyd.ddz.controller;

import com.zyd.ddz.message.login.dto.UserDto;
import com.zyd.ddz.message.room.ReqEnterRoomMessage;
import com.zyd.ddz.service.RoomService;
import com.zyd.ddz.service.UserService;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Controller;
import xyz.noark.core.annotation.controller.ExecThreadGroup;
import xyz.noark.core.annotation.controller.PacketMapping;
import xyz.noark.core.network.Session;

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
    public void enterRoom(Session session, ReqEnterRoomMessage message){
        UserDto userDto = userService.getById(session, message.getUid());
        if(userDto == null){
            logger.info("用户不存在");
            return;
        }
        roomService.enterRoom(session, message.getUid(), message.getRoomType());
    }
}
