package com.zyd.ddz.common.dao;

import com.zyd.ddz.common.domain.UserDomain;
import xyz.noark.core.annotation.Repository;
import xyz.noark.orm.repository.UniqueCacheRepository;

import java.util.function.Predicate;

/**
 * @author zyd
 * @date 2023/4/7 10:54
 */
@Repository
public class UserDao extends UniqueCacheRepository<UserDomain, Long> {

    public UserDomain getUserBy(Predicate<UserDomain> filter){
        return this.loadAll().stream().filter(filter).findAny().orElse(null);
    }
}
