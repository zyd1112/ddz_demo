package com.zyd.ddz.service.impl;

import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.service.LoginService;
import com.zyd.ddz.utils.IdUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;

import java.util.Date;

/**
 * @author zyd
 * @date 2023/4/7 11:05
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    IdUtils idUtils;

    @Override
    public UserDomain visitorLogin(Session session) {
        UserDomain userDomain = new UserDomain();
        String name = "visitor-" + idUtils.getVisitorId();
        userDomain.setUsername(name);
        userDomain.setNickname(name);
        userDomain.setId(idUtils.generator());
        userDomain.setCreateTime(new Date());
        userDomain.setIp(session.getIp());
        userDomain.setPassword("111");
        return userDomain;
    }
}
