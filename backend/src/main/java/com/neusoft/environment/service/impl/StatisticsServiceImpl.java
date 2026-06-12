package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.environment.entity.GridRegion;
import com.neusoft.environment.mapper.AqiDataMapper;
import com.neusoft.environment.mapper.GridRegionMapper;
import com.neusoft.environment.service.StatisticsService;
import com.neusoft.environment.vo.StatisticsVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 统计服务实现
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private AqiDataMapper aqiDataMapper;

    @Resource
    private GridRegionMapper gridRegionMapper;

    /** 中国省份总数 */
    private static final int TOTAL_PROVINCES = 34;

    /** 大城市总数 */
    private static final int TOTAL_CITIES = 106;

    @Override
    public List<Map<String, Object>> getProvinceStatistics() {
        return aqiDataMapper.selectProvinceStatistics();
    }

    @Override
    public List<Map<String, Object>> getLevelDistribution() {
        return aqiDataMapper.selectLevelDistribution();
    }

    @Override
    public List<Map<String, Object>> getMonthlyTrend() {
        return aqiDataMapper.selectMonthlyTrend();
    }

    @Override
    public Map<String, Object> getRealtimeStatistics() {
        Map<String, Object> result = aqiDataMapper.selectRealtimeStatistics();
        if (result == null) {
            result = new HashMap<>();
            result.put("total_count", 0);
            result.put("good_count", 0);
            result.put("bad_count", 0);
        }
        return result;
    }

    @Override
    public Map<String, Object> getCoverageStatistics() {
        // 统计已覆盖的省
        LambdaQueryWrapper<GridRegion> provinceWrapper = new LambdaQueryWrapper<>();
        provinceWrapper.select(GridRegion::getProvince).groupBy(GridRegion::getProvince);
        int coveredProvinces = gridRegionMapper.selectCount(provinceWrapper).intValue();

        // 统计已覆盖的大城市
        int coveredCities = gridRegionMapper.selectCount(null).intValue();

        BigDecimal provinceRate = BigDecimal.ZERO;
        BigDecimal cityRate = BigDecimal.ZERO;

        if (TOTAL_PROVINCES > 0) {
            provinceRate = BigDecimal.valueOf(coveredProvinces * 100.0 / TOTAL_PROVINCES)
                    .setScale(2, RoundingMode.HALF_UP);
        }
        if (TOTAL_CITIES > 0) {
            cityRate = BigDecimal.valueOf(Math.min(coveredCities * 100.0 / TOTAL_CITIES, 100.00))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total_provinces", TOTAL_PROVINCES);
        result.put("covered_provinces", coveredProvinces);
        result.put("total_cities", TOTAL_CITIES);
        result.put("covered_cities", coveredCities);
        result.put("province_rate", provinceRate);
        result.put("city_rate", cityRate);
        return result;
    }

    @Override
    public StatisticsVO getOverview() {
        return StatisticsVO.builder()
                .provinceStats(getProvinceStatistics())
                .levelDistribution(getLevelDistribution())
                .monthlyTrend(getMonthlyTrend())
                .realtimeStats(getRealtimeStatistics())
                .coverage(buildCoverageStats())
                .build();
    }

    private StatisticsVO.CoverageStats buildCoverageStats() {
        Map<String, Object> coverage = getCoverageStatistics();
        return StatisticsVO.CoverageStats.builder()
                .totalProvinces((Integer) coverage.get("total_provinces"))
                .coveredProvinces((Integer) coverage.get("covered_provinces"))
                .totalCities((Integer) coverage.get("total_cities"))
                .coveredCities((Integer) coverage.get("covered_cities"))
                .provinceRate((BigDecimal) coverage.get("province_rate"))
                .cityRate((BigDecimal) coverage.get("city_rate"))
                .build();
    }
}
