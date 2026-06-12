package com.neusoft.environment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.environment.entity.Complaint;
import com.neusoft.environment.service.ComplaintService;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {

    @Resource
    private ComplaintService complaintService;

    /** 提交投诉 */
    @PostMapping("/submit")
    public ResultVO<?> submit(@RequestBody Complaint complaint, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        complaintService.submitComplaint(complaint, userId);
        return ResultVO.success("投诉提交成功");
    }

    /** 我的投诉列表 */
    @GetMapping("/my")
    public ResultVO<Page<Complaint>> getMyComplaints(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = (Long) authentication.getPrincipal();
        return ResultVO.success(complaintService.getMyComplaints(userId, page, size));
    }

    /** 待处理投诉列表（管理员） */
    @GetMapping("/admin/pending")
    public ResultVO<Page<Complaint>> getPendingList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResultVO.success(complaintService.getPendingList(page, size));
    }

    /** 处理投诉（管理员） */
    @PutMapping("/admin/handle/{id}")
    public ResultVO<?> handle(
            @PathVariable Long id,
            @RequestParam Long handlerId,
            @RequestParam String result,
            @RequestParam Integer status) {
        complaintService.handle(id, handlerId, result, status);
        return ResultVO.success("投诉处理完成");
    }
}
