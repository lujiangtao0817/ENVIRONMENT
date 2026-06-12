package com.neusoft.environment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.environment.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
}
