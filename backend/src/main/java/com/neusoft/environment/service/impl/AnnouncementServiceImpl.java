package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.entity.Announcement;
import com.neusoft.environment.mapper.AnnouncementMapper;
import com.neusoft.environment.service.AnnouncementService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Override
    public Page<Announcement> getPublishedList(Integer page, Integer size) {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getStatus, 1)
               .orderByDesc(Announcement::getIsTop)
               .orderByDesc(Announcement::getPublishTime);
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public Page<Announcement> getAllList(Integer page, Integer size) {
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Announcement::getCreateTime);
        return this.page(new Page<>(page, size), wrapper);
    }

    @Override
    public void publish(Long id, String publisherName) {
        Announcement a = this.getById(id);
        if (a == null) throw new IllegalArgumentException("公告不存在");
        a.setStatus(1);
        a.setPublishTime(new Date());
        a.setPublisherName(publisherName);
        this.updateById(a);
    }

    @Override
    public void revoke(Long id) {
        Announcement a = this.getById(id);
        if (a == null) throw new IllegalArgumentException("公告不存在");
        a.setStatus(2);
        this.updateById(a);
    }

    @Override
    public void create(Announcement announcement, Long publisherId) {
        announcement.setPublisherId(publisherId);
        announcement.setStatus(0);
        this.save(announcement);
    }
}
