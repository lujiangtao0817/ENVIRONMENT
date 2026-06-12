package com.neusoft.environment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.environment.entity.MessageNotification;
import com.neusoft.environment.service.impl.MessageNotificationServiceImpl;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Resource
    private MessageNotificationServiceImpl notificationService;

    /** 获取未读通知数量 */
    @GetMapping("/unread-count")
    public ResultVO<Map<String, Object>> getUnreadCount(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Map<String, Object> result = new HashMap<>();
        result.put("count", notificationService.getUnreadCount(userId));
        return ResultVO.success(result);
    }

    /** 获取未读通知列表 */
    @GetMapping("/unread")
    public ResultVO<Page<MessageNotification>> getUnread(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = (Long) authentication.getPrincipal();
        return ResultVO.success(notificationService.getUnreadNotifications(userId, page, size));
    }

    /** 获取全部通知 */
    @GetMapping("/all")
    public ResultVO<Page<MessageNotification>> getAll(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = (Long) authentication.getPrincipal();
        return ResultVO.success(notificationService.getAllNotifications(userId, page, size));
    }

    /** 标记已读 */
    @PutMapping("/read/{id}")
    public ResultVO<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResultVO.success("已标记为已读");
    }

    /** 全部标记已读 */
    @PutMapping("/read-all")
    public ResultVO<?> markAllAsRead(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        notificationService.markAllAsRead(userId);
        return ResultVO.success("全部标记为已读");
    }
}
