package com.neusoft.environment.controller;

import com.neusoft.environment.service.StatisticsService;
import com.neusoft.environment.vo.ResultVO;
import com.neusoft.environment.vo.StatisticsVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * NEPV 决策者/可视化大屏控制器
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    /**
     * 大屏综合数据概览（包含全部8项统计）
     * GET /api/statistics/overview
     */
    @GetMapping("/overview")
    public ResultVO<StatisticsVO> getOverview() {
        StatisticsVO overview = statisticsService.getOverview();
        return ResultVO.success(overview);
    }
}
