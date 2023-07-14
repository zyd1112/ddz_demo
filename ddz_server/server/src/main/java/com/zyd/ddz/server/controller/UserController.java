package com.zyd.ddz.server.controller;

import com.zyd.ddz.common.constant.ResponseCode;
import com.zyd.ddz.common.domain.UserDomain;
import com.zyd.ddz.common.entity.UserMail;
import com.zyd.ddz.common.message.login.response.ResLoginMessage;
import com.zyd.ddz.common.utils.*;
import com.zyd.ddz.common.service.LoginService;
import com.zyd.ddz.common.service.UserService;
import com.zyd.zgame.common.annotation.Controller;
import com.zyd.zgame.common.annotation.Exposed;
import com.zyd.zgame.common.constant.ThreadModuleGroup;
import com.zyd.zgame.common.network.Session;
import com.zyd.zgame.common.network.SessionManager;
import com.zyd.zgame.common.utils.ApplicationContextUtils;
import com.zyd.zgame.common.utils.IdUtils;
import com.zyd.zgame.common.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author zyd
 * @date 2023/4/7 11:00
 */
@Controller(threadGroup = ThreadModuleGroup.COMMON_THREAD)
@Slf4j
public class UserController {

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @Exposed(id = 11)
    public void visitorLogin(Session session){
        ResLoginMessage loginMessage = new ResLoginMessage();
        try {
            String name = "visitor-" + IdUtils.getVisitorId();
            UserDomain userDomain = loginService.login(session, name, name, "111");
            SessionManager.bindRid(userDomain.getId(), session);
            SessionManager.bindUid(userDomain.getId(), session);
            session.setRid(userDomain.getId());
            session.setUid(userDomain.getId());
            loginMessage.setCode(ResponseCode.SUCCESS.getCode());
            loginMessage.setUser(DtoUtils.packUser(userDomain));
        }catch (Exception e){
            log.error("登录失败, {}", e.getMessage());
            loginMessage.setCode(ResponseCode.FAIL.getCode());
        }
        session.sendMessage(loginMessage.getOpcode(), loginMessage);
    }

    @Exposed(id = 8)
    public void getCode(UserMail userMail){
        String code = MailUtils.generateCode(4);
        userMail.setCode(code);
        userMail.setEnd(TimeUtils.getNowTimeMillis() + 120000);
        MailUtils.sendMail(userMail);
    }

    @Exposed(id = 9)
    public void mailLogin(Session session, UserMail userMail){
        UserMail mail = MailUtils.getMAILS_MAP().get(userMail.getMail());
        ResLoginMessage loginMessage = new ResLoginMessage();
        try {
            if (!MailUtils.checkTimeout(userMail)) {
                log.error("{}, 验证码过期", userMail.getMail());
                MailUtils.getMAILS_MAP().remove(userMail.getMail());
                loginMessage.setCode(ResponseCode.CODE_EXPIRED.getCode());
                loginMessage.setContent(ResponseCode.CODE_EXPIRED.getContent());
            }else if(!mail.getCode().equals(userMail.getCode())){
                log.error("{}, 验证码错误", userMail.getMail());
                loginMessage.setCode(ResponseCode.CODE_ERROR.getCode());
                loginMessage.setContent(ResponseCode.CODE_ERROR.getContent());
            }else {
                UserDomain userDomain = loginService.login(session, userMail.getMail(), userMail.getMail(), "");
                SessionManager.bindUid(userDomain.getId(), session);
                SessionManager.bindRid(userDomain.getId(), session);
                session.setUid(userDomain.getId());
                session.setRid(userDomain.getId());
                loginMessage.setCode(ResponseCode.SUCCESS.getCode());
                loginMessage.setContent(ResponseCode.SUCCESS.getContent());

                loginMessage.setUser(DtoUtils.packUser(userDomain));
            }
        }catch (Exception e){
            log.info("登录失败, {}", e.getMessage());
            loginMessage.setCode(ResponseCode.FAIL.getCode());
            loginMessage.setContent(ResponseCode.FAIL.getContent());
        }
        session.sendMessage(loginMessage.getOpcode(), loginMessage);
    }

}
