package com.zyd.ddz.common.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyd.ddz.common.domain.UserDomain;
import org.springframework.stereotype.Component;
/**
 * @author zyd
 * @date 2023/4/7 10:54
 */
@Component
public class UserDao extends ServiceImpl<UserMapper, UserDomain> {
}
