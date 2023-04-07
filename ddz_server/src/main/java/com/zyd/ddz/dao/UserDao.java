package com.zyd.ddz.dao;

import com.zyd.ddz.domain.UserDomain;
import xyz.noark.core.annotation.Repository;
import xyz.noark.orm.repository.UniqueCacheRepository;

/**
 * @author zyd
 * @date 2023/4/7 10:54
 */
@Repository
public class UserDao extends UniqueCacheRepository<UserDomain, Long> {
}
