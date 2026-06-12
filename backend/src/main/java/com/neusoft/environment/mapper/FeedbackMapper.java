package com.neusoft.environment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.environment.entity.SupervisionFeedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * 监督反馈 Mapper
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<SupervisionFeedback> {
}
