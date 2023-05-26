package com.zyd.ddz.common.service;

import com.zyd.ddz.common.domain.UserDomain;
import com.zyd.ddz.common.entity.UserMail;
import xyz.noark.core.network.Session;

/**
 * @author zyd
 * @date 2023/4/7 11:03
 */
public interface LoginService {

    /**
     * 登录
     */
    UserDomain login(Session session, String username, String nickname, String password);

}
