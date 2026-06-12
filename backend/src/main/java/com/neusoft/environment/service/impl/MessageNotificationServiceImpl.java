package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.entity.MessageNotification;
import com.neusoft.environment.mapper.MessageNotificationMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageNotificationServiceImpl extends ServiceImpl<MessageNotificationMapper, MessageNotification> {

    /** 发送通知 */
    public void sendNotification(Long userId, String title, String content, Integer type, Long relatedId) {
        MessageNotification notification = new MessageNotification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        this.save(notification);
    }

    /** 获取未读通知 */
    public Page<MessageNotification> getUnreadNotifications(Long userId, Integer page, Integer size) {
        LambdaQueryWrapper<MessageNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageNotification::getUserId, userId)
               .eq(MessageNotification::getIsRead, 0)
               .orderByDesc(MessageNotification::getCreateTime);
        return this.page(new Page<>(page, size), wrapper);
    }

    /** 获取全部通知 */
    public Page<MessageNotification> getAllNotifications(Long userId, Integer page, Integer size) {
        LambdaQueryWrapper<MessageNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageNotification::getUserId, userId)
               .orderByDesc(MessageNotification::getCreateTime);
        return this.page(new Page<>(page, size), wrapper);
    }

    /** 标记已读 */
    public void markAsRead(Long id) {
        MessageNotification n = this.getById(id);
        if (n != null) {
            n.setIsRead(1);
            n.setReadTime(new Date());
            this.updateById(n);
        }
    }

    /** 标记全部已读 */
    public void markAllAsRead(Long userId) {
        LambdaQueryWrapper<MessageNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageNotification::getUserId, userId)
               .eq(MessageNotification::getIsRead, 0);
        this.list(wrapper).forEach(n -> {
            n.setIsRead(1);
            n.setReadTime(new Date());
            this.updateById(n);
        });
    }

    /** 获取未读数量 */
    public long getUnreadCount(Long userId) {
        LambdaQueryWrapper<MessageNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MessageNotification::getUserId, userId)
               .eq(MessageNotification::getIsRead, 0);
        return this.count(wrapper);
    }
}
