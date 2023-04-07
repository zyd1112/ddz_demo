package com.zyd.ddz.service.impl;

import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.service.LoginService;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;

/**
 * @author zyd
 * @date 2023/4/7 11:05
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDomain visitorLogin(Session session, String username, String password) {
        for (UserDomain userDomain : userDao.loadAll()) {
            if(userDomain.getUsername().equals(username) && userDomain.getPassword().equals(password)){
                return userDomain;
            }
        }
        return null;
    }
}
