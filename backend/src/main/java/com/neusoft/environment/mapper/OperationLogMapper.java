package com.neusoft.environment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.environment.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
