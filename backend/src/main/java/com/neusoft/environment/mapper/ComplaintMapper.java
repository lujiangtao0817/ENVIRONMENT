package com.neusoft.environment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.environment.entity.Complaint;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ComplaintMapper extends BaseMapper<Complaint> {
}
