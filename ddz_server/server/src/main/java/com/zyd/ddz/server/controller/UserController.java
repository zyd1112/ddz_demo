package com.zyd.ddz.server.controller;

import com.zyd.ddz.common.constant.ResponseCode;
import com.zyd.ddz.common.domain.UserDomain;
import com.zyd.ddz.common.entity.UserMail;
import com.zyd.ddz.common.message.login.response.ResLoginMessage;
import com.zyd.ddz.common.utils.*;
import com.zyd.ddz.common.service.LoginService;
import com.zyd.ddz.common.service.UserService;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Controller;
import xyz.noark.core.annotation.controller.ExecThreadGroup;
import xyz.noark.core.annotation.controller.PacketMapping;
import xyz.noark.core.network.Session;
import xyz.noark.core.network.SessionManager;
import xyz.noark.log.LogHelper;
import xyz.noark.network.WebSocketSession;


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

    @PacketMapping(opcode = 11, state = Session.State.CONNECTED)
    public void visitorLogin(Session session){
        ResLoginMessage loginMessage = new ResLoginMessage();
        try {
            String name = "visitor-" + IdUtils.getVisitorId();
            UserDomain userDomain = loginService.login(session, name, name, "111");
            SessionManager.bindPlayerIdAndSession(userDomain.getId(), session);
            ((WebSocketSession) session).setPlayerId(userDomain.getId());
            loginMessage.setCode(ResponseCode.SUCCESS.getCode());
            loginMessage.setUser(DtoUtils.packUser(userDomain));
        }catch (Exception e){
            LogHelper.logger.info("登录失败, {}", e);
            loginMessage.setCode(ResponseCode.FAIL.getCode());
        }
        session.send(loginMessage.getOpcode(), loginMessage);
    }

    @PacketMapping(opcode = 8, state = Session.State.CONNECTED)
    public void getCode(UserMail userMail){
        String code = MailUtils.generateCode(4);
        userMail.setCode(code);
        userMail.setEnd(TimeUtils.getNowTimeMillis() + 120000);
        MailUtils.sendMail(userMail);
    }

    @PacketMapping(opcode = 9, state = Session.State.CONNECTED)
    public void mailLogin(Session session, UserMail userMail){
        UserMail mail = MailUtils.getMAILS_MAP().get(userMail.getMail());
        ResLoginMessage loginMessage = new ResLoginMessage();
        try {
            if (!MailUtils.checkTimeout(userMail)) {
                LogHelper.logger.error("{}, 验证码过期", userMail.getMail());
                MailUtils.getMAILS_MAP().remove(userMail.getMail());
                loginMessage.setCode(ResponseCode.CODE_EXPIRED.getCode());
                loginMessage.setContent(ResponseCode.CODE_EXPIRED.getContent());
            }else if(!mail.getCode().equals(userMail.getCode())){
                LogHelper.logger.error("{}, 验证码错误", userMail.getMail());
                loginMessage.setCode(ResponseCode.CODE_ERROR.getCode());
                loginMessage.setContent(ResponseCode.CODE_ERROR.getContent());
            }else {
                UserDomain userDomain = loginService.login(session, userMail.getMail(), userMail.getMail(), "");
                SessionManager.bindPlayerIdAndSession(userDomain.getId(), session);
                ((WebSocketSession) session).setPlayerId(userDomain.getId());
                loginMessage.setCode(ResponseCode.SUCCESS.getCode());
                loginMessage.setContent(ResponseCode.SUCCESS.getContent());

                loginMessage.setUser(DtoUtils.packUser(userDomain));
            }
        }catch (Exception e){
            LogHelper.logger.info("登录失败, {}", e);
            loginMessage.setCode(ResponseCode.FAIL.getCode());
            loginMessage.setContent(ResponseCode.FAIL.getContent());
        }
        session.send(loginMessage.getOpcode(), loginMessage);
    }

}
