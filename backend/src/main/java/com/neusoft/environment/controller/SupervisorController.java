package com.neusoft.environment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.environment.dto.FeedbackSubmitRequest;
import com.neusoft.environment.service.FeedbackService;
import com.neusoft.environment.service.GridRegionService;
import com.neusoft.environment.vo.FeedbackVO;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * NEPS 公众监督员控制器
 */
@RestController
@RequestMapping("/api/supervisor")
public class SupervisorController {

    @Resource
    private GridRegionService gridRegionService;

    @Resource
    private FeedbackService feedbackService;

    /**
     * 获取可选网格区域（省市树形结构）
     * GET /api/supervisor/regions
     */
    @GetMapping("/regions")
    public ResultVO<List<Map<String, Object>>> getRegions() {
        List<Map<String, Object>> tree = gridRegionService.getRegionTree();
        return ResultVO.success(tree);
    }

    /**
     * 提交空气质量监督反馈
     * POST /api/supervisor/feedback
     */
    @PostMapping("/feedback")
    public ResultVO<?> submitFeedback(Authentication authentication,
                                      @Valid @RequestBody FeedbackSubmitRequest request) {
        Long supervisorId = (Long) authentication.getPrincipal();
        feedbackService.submitFeedback(supervisorId, request);
        return ResultVO.success("反馈提交成功");
    }

    /**
     * 浏览我的历史反馈列表
     * GET /api/supervisor/feedback/my
     */
    @GetMapping("/feedback/my")
    public ResultVO<Page<FeedbackVO>> getMyFeedback(
            Authentication authentication,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long supervisorId = (Long) authentication.getPrincipal();
        Page<FeedbackVO> result = feedbackService.getMyFeedback(supervisorId, page, size);
        return ResultVO.success(result);
    }

    /**
     * 浏览某条反馈详情
     * GET /api/supervisor/feedback/{id}
     */
    @GetMapping("/feedback/{id}")
    public ResultVO<FeedbackVO> getFeedbackDetail(@PathVariable Long id) {
        FeedbackVO vo = feedbackService.getFeedbackDetail(id);
        return ResultVO.success(vo);
    }
}
