package com.zyd.ddz.service.impl;

import com.zyd.ddz.dao.UserDao;
import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.message.login.dto.UserDto;
import com.zyd.ddz.service.UserService;
import com.zyd.ddz.utils.DtoUtils;
import xyz.noark.core.annotation.Autowired;
import xyz.noark.core.annotation.Service;
import xyz.noark.core.network.Session;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zyd
 * @date 2023/4/7 15:06
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDto getById(Session session, long uid) {
        return DtoUtils.packUser(userDao.cacheGet(uid));
    }

    @Override
    public List<UserDto> getList(Session session) {
        return userDao.loadAll().stream().map(DtoUtils::packUser).collect(Collectors.toList());
    }
}
