package com.neusoft.environment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.environment.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
}
