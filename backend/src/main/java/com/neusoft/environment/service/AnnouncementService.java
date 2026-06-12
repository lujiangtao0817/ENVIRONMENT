package com.neusoft.environment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.environment.entity.Announcement;

public interface AnnouncementService extends IService<Announcement> {
    Page<Announcement> getPublishedList(Integer page, Integer size);
    Page<Announcement> getAllList(Integer page, Integer size);
    void publish(Long id, String publisherName);
    void revoke(Long id);
    void create(Announcement announcement, Long publisherId);
}
