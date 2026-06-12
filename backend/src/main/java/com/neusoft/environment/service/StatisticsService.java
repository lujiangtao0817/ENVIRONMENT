package com.neusoft.environment.service;

import com.neusoft.environment.vo.StatisticsVO;

import java.util.List;
import java.util.Map;

/**
 * 统计服务接口
 */
public interface StatisticsService {

    /**
     * 省分组超标统计
     */
    List<Map<String, Object>> getProvinceStatistics();

    /**
     * AQI指数分布统计
     */
    List<Map<String, Object>> getLevelDistribution();

    /**
     * AQI指数趋势统计（近12个月）
     */
    List<Map<String, Object>> getMonthlyTrend();

    /**
     * 实时统计
     */
    Map<String, Object> getRealtimeStatistics();

    /**
     * 网格覆盖率统计
     */
    Map<String, Object> getCoverageStatistics();

    /**
     * 大屏综合概览
     */
    StatisticsVO getOverview();
}
