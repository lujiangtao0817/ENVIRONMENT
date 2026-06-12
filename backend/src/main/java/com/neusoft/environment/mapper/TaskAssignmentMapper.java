package com.neusoft.environment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.environment.entity.TaskAssignment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务指派 Mapper
 */
@Mapper
public interface TaskAssignmentMapper extends BaseMapper<TaskAssignment> {
}
