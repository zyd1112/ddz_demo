package com.zyd.ddz.message.login.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/7 11:14
 */
@Setter
@Getter
public class UserDto {

    private long id;

    private String username;

    private String password;

    private String nickname;

    private int joyBeans;

    private int imageIndex;

}
