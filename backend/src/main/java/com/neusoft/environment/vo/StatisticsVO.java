package com.neusoft.environment.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 统计概览视图（大屏用）
 */
@Data
@Builder
public class StatisticsVO {

    /** 省分组超标统计 */
    private List<Map<String, Object>> provinceStats;

    /** AQI指数分布统计 */
    private List<Map<String, Object>> levelDistribution;

    /** AQI指数趋势统计（近12个月） */
    private List<Map<String, Object>> monthlyTrend;

    /** 实时统计 */
    private Map<String, Object> realtimeStats;

    /** 网格覆盖率 */
    private CoverageStats coverage;

    @Data
    @Builder
    public static class CoverageStats {
        private Integer totalProvinces;
        private Integer coveredProvinces;
        private Integer totalCities;
        private Integer coveredCities;
        private BigDecimal provinceRate;
        private BigDecimal cityRate;
    }
}
