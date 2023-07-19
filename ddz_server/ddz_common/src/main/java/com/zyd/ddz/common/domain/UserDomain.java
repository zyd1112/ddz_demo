package com.zyd.ddz.common.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zyd
 * @date 2023/4/7 9:50
 */

@TableName(value = "game_user")
@Setter
@Getter
public class UserDomain {

    @TableId
    Long id;

    String username;

    String password;

    String nickname;

    Date createTime;

    String ip;

    Integer joyBeans;

    Integer imageIndex;
}
