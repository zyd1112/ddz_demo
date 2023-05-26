package com.zyd.ddz.server.service.impl;

import com.zyd.ddz.common.entity.UserMail;
import com.zyd.ddz.common.utils.IdUtils;
import com.zyd.ddz.common.dao.UserDao;
import com.zyd.ddz.common.domain.UserDomain;
import com.zyd.ddz.common.service.LoginService;
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
    public UserDomain login(Session session, String username, String nickname, String password) {
        UserDomain userDomain = userDao.getUserBy(user -> user.getUsername().equals(username));
        if(userDomain == null){
            userDomain = new UserDomain();
            userDomain.setUsername(username);
            userDomain.setNickname(nickname);
            userDomain.setId(IdUtils.generator());
            userDomain.setCreateTime(new Date());
            userDomain.setIp(session.getIp());
            userDomain.setPassword(password);
            userDomain.setImageIndex(RandomUtils.nextInt(3));
            userDao.insert(userDomain);
        }
        return userDomain;
    }
}
