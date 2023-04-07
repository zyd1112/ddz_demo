package com.zyd.ddz.message.login;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zyd
 * @date 2023/4/7 11:02
 */
@Setter
@Getter
public class ReqLoginMessage {

    private String username;

    private String password;
}
