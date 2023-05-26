package com.zyd.ddz.common.message.login.response;


import com.zyd.ddz.common.message.Message;
import com.zyd.ddz.common.message.login.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/25 13:19
 */
@Getter
@Setter
public class ResLoginMessage implements Message {
    int opcode = 1002;
    int code;

    UserDto user;
}
