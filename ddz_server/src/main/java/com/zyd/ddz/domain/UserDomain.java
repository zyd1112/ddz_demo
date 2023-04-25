package com.zyd.ddz.domain;

import lombok.Getter;
import lombok.Setter;
import xyz.noark.core.annotation.orm.Column;
import xyz.noark.core.annotation.orm.Entity;
import xyz.noark.core.annotation.orm.Id;
import xyz.noark.core.annotation.orm.Table;

import java.util.Date;

/**
 * @author zyd
 * @date 2023/4/7 9:50
 */

@Entity
@Table(name = "game_user")
@Setter
@Getter
public class UserDomain {

    @Id
    @Column(name = "id", nullable = false, comment = "用户id", length = 64)
    private long id;

    @Column(name = "username", nullable = false, comment = "用户名", length = 20)
    private String username;

    @Column(name = "password", nullable = false, comment = "用户密码", length = 20)
    private String password;

    @Column(name = "nickname", nullable = false, comment = "用户昵称", length = 20)
    private String nickname;

    @Column(name = "create_time", nullable = false, comment = "创建时间")
    private Date createTime;

    @Column(name = "ip", nullable = false, comment = "ip")
    private String ip;

    @Column(name = "joy_beans", nullable = false, comment = "欢乐豆")
    private int joyBeans;

    @Column(name = "image_index", nullable = false, comment = "头像索引")
    private int imageIndex;
}
