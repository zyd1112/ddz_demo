package com.zyd.ddz.server.service.impl;

import com.zyd.ddz.common.domain.UserDomain;
import com.zyd.ddz.common.message.login.dto.UserDto;
import com.zyd.ddz.common.utils.DtoUtils;
import com.zyd.ddz.common.service.UserService;
import com.zyd.zgame.common.network.Session;
import com.zyd.zgame.orm.cache.DataContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zyd
 * @date 2023/4/7 15:06
 */
@Component
public class UserServiceImpl implements UserService {

    @Override
    public UserDto getById(Session session, long uid) {
        return DtoUtils.packUser(DataContext.getManager().getById(UserDomain.class, uid));
    }

    @Override
    public List<UserDto> getList(Session session) {
        return DataContext.getManager().getAll(UserDomain.class).stream().map(DtoUtils::packUser).collect(Collectors.toList());
    }
}
