package com.zyd.ddz.common.dao;

import com.zyd.ddz.common.domain.UserDomain;
import com.zyd.zgame.orm.cache.mapper.AbstractCacheService;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

/**
 * @author zyd
 * @date 2023/4/7 10:54
 */
@Component
public class UserDao extends AbstractCacheService<UserMapper, UserDomain> {

    public UserDomain getUserBy(Predicate<UserDomain> filter){
        return list().stream().filter(filter).findAny().orElse(null);
    }
}
