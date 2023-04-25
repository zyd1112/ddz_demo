package com.zyd.ddz.utils;

import com.zyd.ddz.domain.UserDomain;
import com.zyd.ddz.message.login.dto.UserDto;
import xyz.noark.core.util.RandomUtils;

/**
 * @author zyd
 * @date 2023/4/7 11:17
 */
public class DtoUtils {

    public static UserDto packUser(UserDomain userDomain){
        UserDto userDto = new UserDto();
        userDto.setId(userDomain.getId());
        userDto.setUsername(userDomain.getUsername());
        userDto.setPassword(userDomain.getPassword());
        userDto.setNickname(userDomain.getNickname());
        userDto.setNickname(userDomain.getNickname());
        userDto.setJoyBeans(userDomain.getJoyBeans());
        userDto.setImageIndex(userDomain.getImageIndex());
        return userDto;
    }
}
