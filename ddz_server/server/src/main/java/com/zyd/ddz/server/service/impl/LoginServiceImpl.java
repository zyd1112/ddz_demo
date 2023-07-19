package com.zyd.ddz.server.service.impl;

import com.zyd.ddz.common.domain.UserDomain;
import com.zyd.ddz.common.service.LoginService;
import com.zyd.zgame.common.network.Session;
import com.zyd.zgame.common.utils.IdUtils;
import com.zyd.zgame.common.utils.RandomUtils;
import com.zyd.zgame.orm.cache.DataContext;
import org.springframework.stereotype.Component;


import java.util.Date;

/**
 * @author zyd
 * @date 2023/4/7 11:05
 */
@Component
public class LoginServiceImpl implements LoginService {


    @Override
    public UserDomain login(Session session, String username, String nickname, String password) {
        UserDomain userDomain = DataContext.getManager().getAll(UserDomain.class)
                .stream().filter(user -> user.getUsername().equals(username)).findAny().orElse(null);
        if(userDomain == null){
            userDomain = new UserDomain();
            userDomain.setUsername(username);
            userDomain.setNickname(nickname);
            userDomain.setId(IdUtils.generator());
            userDomain.setCreateTime(new Date());
            userDomain.setIp(session.getIp());
            userDomain.setPassword(password);
            userDomain.setImageIndex(RandomUtils.random(0, 3));
            DataContext.getManager().update(userDomain);
        }
        return userDomain;
    }
}
