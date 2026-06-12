package com.neusoft.environment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.environment.dto.AssignRequest;
import com.neusoft.environment.entity.SysUser;
import com.neusoft.environment.service.*;
import com.neusoft.environment.vo.AqiDataVO;
import com.neusoft.environment.vo.FeedbackVO;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * NEPM 系统管理员控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Resource
    private FeedbackService feedbackService;

    @Resource
    private TaskAssignmentService taskAssignmentService;

    @Resource
    private AqiDataService aqiDataService;

    @Resource
    private StatisticsService statisticsService;

    // ==================== 公众监督数据管理 ====================

    /**
     * 浏览公众监督反馈数据列表（支持条件查询）
     * GET /api/admin/feedback/list
     */
    @GetMapping("/feedback/list")
    public ResultVO<Page<FeedbackVO>> getFeedbackList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Integer status) {
        Page<FeedbackVO> result = feedbackService.getFeedbackList(page, size, province, city, status);
        return ResultVO.success(result);
    }

    /**
     * 浏览反馈数据详情
     * GET /api/admin/feedback/{id}
     */
    @GetMapping("/feedback/{id}")
    public ResultVO<FeedbackVO> getFeedbackDetail(@PathVariable Long id) {
        FeedbackVO vo = feedbackService.getFeedbackDetail(id);
        return ResultVO.success(vo);
    }

    /**
     * 指派网格员
     * POST /api/admin/feedback/assign
     */
    @PostMapping("/feedback/assign")
    public ResultVO<?> assignGridMan(@Valid @RequestBody AssignRequest request) {
        taskAssignmentService.assignGridMan(request.getFeedbackId(), request.getGridManId());
        return ResultVO.success("指派成功");
    }

    /**
     * 获取可用的网格员列表
     * GET /api/admin/gridman/available
     */
    @GetMapping("/gridman/available")
    public ResultVO<List<SysUser>> getAvailableGridMen(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city) {
        List<SysUser> gridMen = taskAssignmentService.getAvailableGridMen(province, city);
        return ResultVO.success(gridMen);
    }

    /**
     * 推荐最佳网格员
     * GET /api/admin/gridman/recommend/{feedbackId}
     */
    @GetMapping("/gridman/recommend/{feedbackId}")
    public ResultVO<SysUser> recommendGridMan(@PathVariable Long feedbackId) {
        SysUser gridMan = taskAssignmentService.recommendGridMan(feedbackId);
        return ResultVO.success(gridMan);
    }

    // ==================== AQI数据管理 ====================

    /**
     * 浏览网格员提交的AQI数据列表
     * GET /api/admin/aqi/list
     */
    @GetMapping("/aqi/list")
    public ResultVO<Page<AqiDataVO>> getAqiDataList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String city) {
        Page<AqiDataVO> result = aqiDataService.getAqiDataList(page, size, province, city);
        return ResultVO.success(result);
    }

    /**
     * 浏览AQI数据详情
     * GET /api/admin/aqi/{id}
     */
    @GetMapping("/aqi/{id}")
    public ResultVO<AqiDataVO> getAqiDataDetail(@PathVariable Long id) {
        AqiDataVO vo = aqiDataService.getAqiDataDetail(id);
        return ResultVO.success(vo);
    }

    // ==================== 统计数据管理 ====================

    /**
     * 省分组超标统计
     * GET /api/admin/statistics/province
     */
    @GetMapping("/statistics/province")
    public ResultVO<List<Map<String, Object>>> getProvinceStatistics() {
        List<Map<String, Object>> result = statisticsService.getProvinceStatistics();
        return ResultVO.success(result);
    }

    /**
     * AQI指数分布统计
     * GET /api/admin/statistics/level-distribution
     */
    @GetMapping("/statistics/level-distribution")
    public ResultVO<List<Map<String, Object>>> getLevelDistribution() {
        List<Map<String, Object>> result = statisticsService.getLevelDistribution();
        return ResultVO.success(result);
    }

    /**
     * AQI指数趋势统计（近12个月）
     * GET /api/admin/statistics/trend
     */
    @GetMapping("/statistics/trend")
    public ResultVO<List<Map<String, Object>>> getMonthlyTrend() {
        List<Map<String, Object>> result = statisticsService.getMonthlyTrend();
        return ResultVO.success(result);
    }

    /**
     * 空气质量检测数量实时统计
     * GET /api/admin/statistics/realtime
     */
    @GetMapping("/statistics/realtime")
    public ResultVO<Map<String, Object>> getRealtimeStatistics() {
        Map<String, Object> result = statisticsService.getRealtimeStatistics();
        return ResultVO.success(result);
    }

    /**
     * 全国网格覆盖率统计
     * GET /api/admin/statistics/coverage
     */
    @GetMapping("/statistics/coverage")
    public ResultVO<Map<String, Object>> getCoverageStatistics() {
        Map<String, Object> result = statisticsService.getCoverageStatistics();
        return ResultVO.success(result);
    }
}
