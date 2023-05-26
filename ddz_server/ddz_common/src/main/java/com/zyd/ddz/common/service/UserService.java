package com.zyd.ddz.common.service;

import com.zyd.ddz.common.message.login.dto.UserDto;
import xyz.noark.core.network.Session;

import java.util.List;

/**
 * @author zyd
 * @date 2023/4/7 15:04
 */
public interface UserService {

    UserDto getById(Session session, long uid);

    List<UserDto> getList(Session session);
}
