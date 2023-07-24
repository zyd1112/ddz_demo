package com.zyd.ddz.common.dao;

import com.zyd.ddz.common.domain.UserDomain;
import com.zyd.zgame.orm.cache.action.LocalCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zyd
 * @date 2023/7/24 14:20
 */
@Component
public class UserCache extends LocalCache<Long, UserDomain> {

    @Autowired
    UserDao userDao;

    @Override
    public long expiredTime() {
        return 1;
    }

    @Override
    public TimeUnit unit() {
        return TimeUnit.DAYS;
    }
}
