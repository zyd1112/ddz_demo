package com.zyd.ddz.controller;

import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.service.LoginService;
import com.zyd.ddz.service.UserService;
import com.zyd.ddz.utils.DtoUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Controller;
import xyz.noark.core.annotation.controller.ExecThreadGroup;
import xyz.noark.core.annotation.controller.PacketMapping;
import xyz.noark.core.network.Session;


/**
 * @author zyd
 * @date 2023/4/7 11:00
 */
@Controller(threadGroup = ExecThreadGroup.ModuleThreadGroup)
public class UserController {

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @PacketMapping(opcode = 1001, state = Session.State.CONNECTED)
    public void visitorLogin(Session session){
        UserDomain userDomain = loginService.visitorLogin(session);

        session.send(1001, DtoUtils.packUser(userDomain));
    }

    @PacketMapping(opcode = 1002, state = Session.State.CONNECTED)
    public void enterRoom(Session session, long uid){

    }
}
