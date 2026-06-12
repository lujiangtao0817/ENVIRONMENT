package com.neusoft.environment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.environment.entity.Announcement;
import com.neusoft.environment.service.AnnouncementService;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    /** 获取已发布公告列表（公开） */
    @GetMapping("/list")
    public ResultVO<Page<Announcement>> getPublishedList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResultVO.success(announcementService.getPublishedList(page, size));
    }

    /** 获取所有公告列表（管理员） */
    @GetMapping("/admin/list")
    public ResultVO<Page<Announcement>> getAllList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResultVO.success(announcementService.getAllList(page, size));
    }

    /** 创建公告（管理员） */
    @PostMapping("/admin/create")
    public ResultVO<?> create(@RequestBody Announcement announcement, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        announcementService.create(announcement, userId);
        return ResultVO.success("公告创建成功");
    }

    /** 发布公告（管理员） */
    @PutMapping("/admin/publish/{id}")
    public ResultVO<?> publish(@PathVariable Long id) {
        announcementService.publish(id, null);
        return ResultVO.success("公告已发布");
    }

    /** 撤回公告（管理员） */
    @PutMapping("/admin/revoke/{id}")
    public ResultVO<?> revoke(@PathVariable Long id) {
        announcementService.revoke(id);
        return ResultVO.success("公告已撤回");
    }
}
