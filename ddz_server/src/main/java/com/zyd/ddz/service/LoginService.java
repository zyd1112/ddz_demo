package com.zyd.ddz.service;

import com.zyd.ddz.domain.UserDomain;
import xyz.noark.core.network.Session;

/**
 * @author zyd
 * @date 2023/4/7 11:03
 */
public interface LoginService {

    /**
     * 游客登录
     */
    UserDomain visitorLogin(Session session, String username, String password);
}
