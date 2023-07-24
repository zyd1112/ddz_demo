package com.zyd.ddz.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyd.ddz.common.domain.UserDomain;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zyd
 * @date 2023/7/14 11:39
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDomain> {
}
