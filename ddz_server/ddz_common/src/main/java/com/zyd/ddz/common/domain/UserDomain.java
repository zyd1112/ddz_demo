package com.zyd.ddz.common.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zyd.zgame.orm.cache.Cache;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author zyd
 * @date 2023/4/7 9:50
 */

@TableName(value = "game_user")
@Setter
@Getter
public class UserDomain implements Cache {

    @Id
    private Long id;

    private String username;

    private String password;

    private String nickname;

    private Date createTime;

    private String ip;

    private int joyBeans;

    private int imageIndex;
}
