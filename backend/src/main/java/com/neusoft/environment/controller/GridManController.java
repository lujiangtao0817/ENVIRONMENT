package com.neusoft.environment.controller;

import com.neusoft.environment.dto.AqiSubmitRequest;
import com.neusoft.environment.service.AqiDataService;
import com.neusoft.environment.service.TaskAssignmentService;
import com.neusoft.environment.vo.FeedbackVO;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * NEPG 网格员控制器
 */
@RestController
@RequestMapping("/api/gridman")
public class GridManController {

    @Resource
    private TaskAssignmentService taskAssignmentService;

    @Resource
    private AqiDataService aqiDataService;

    /**
     * 浏览指派给我的任务列表
     * GET /api/gridman/tasks
     */
    @GetMapping("/tasks")
    public ResultVO<List<FeedbackVO>> getMyTasks(Authentication authentication) {
        Long gridManId = (Long) authentication.getPrincipal();
        List<FeedbackVO> tasks = taskAssignmentService.getMyTasks(gridManId);
        return ResultVO.success(tasks);
    }

    /**
     * 浏览任务详情（含监督员反馈信息）
     * GET /api/gridman/tasks/{id}
     */
    @GetMapping("/tasks/{id}")
    public ResultVO<FeedbackVO> getTaskDetail(@PathVariable Long id) {
        FeedbackVO vo = taskAssignmentService.getTaskDetail(id);
        return ResultVO.success(vo);
    }

    /**
     * 提交实测AQI数据
     * POST /api/gridman/aqi/submit
     */
    @PostMapping("/aqi/submit")
    public ResultVO<?> submitAqiData(Authentication authentication,
                                     @Valid @RequestBody AqiSubmitRequest request) {
        Long gridManId = (Long) authentication.getPrincipal();
        aqiDataService.submitAqiData(gridManId, request);
        return ResultVO.success("AQI数据提交成功，任务已完成");
    }
}
