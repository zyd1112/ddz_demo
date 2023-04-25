package com.zyd.ddz.service.impl;

import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.service.LoginService;
import com.zyd.ddz.utils.IdUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;
import xyz.noark.core.util.RandomUtils;

import java.util.Date;

/**
 * @author zyd
 * @date 2023/4/7 11:05
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDomain visitorLogin(Session session) {
        UserDomain userDomain = new UserDomain();
        String name = "visitor-" + IdUtils.getVisitorId();
        userDomain.setUsername(name);
        userDomain.setNickname(name);
        userDomain.setId(IdUtils.generator());
        userDomain.setCreateTime(new Date());
        userDomain.setIp(session.getIp());
        userDomain.setPassword("111");
        userDomain.setImageIndex(RandomUtils.nextInt(3));
        userDao.insert(userDomain);
        return userDomain;
    }
}
