package com.neusoft.environment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.environment.entity.AqiData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * AQI数据 Mapper
 */
@Mapper
public interface AqiDataMapper extends BaseMapper<AqiData> {

    /**
     * 省分组统计 - 各省超标累计数量
     */
    @Select("SELECT " +
            "  u.grid_province as province, " +
            "  COUNT(*) as total_count, " +
            "  SUM(CASE WHEN ad.is_exceeded = 1 THEN 1 ELSE 0 END) as exceeded_count, " +
            "  SUM(CASE WHEN ad.so2_aqi >= 3 THEN 1 ELSE 0 END) as so2_exceeded, " +
            "  SUM(CASE WHEN ad.co_aqi >= 3 THEN 1 ELSE 0 END) as co_exceeded, " +
            "  SUM(CASE WHEN ad.pm25_aqi >= 3 THEN 1 ELSE 0 END) as pm25_exceeded " +
            "FROM aqi_data ad " +
            "LEFT JOIN task_assignment ta ON ad.feedback_id = ta.feedback_id " +
            "LEFT JOIN supervision_feedback sf ON ad.feedback_id = sf.id " +
            "LEFT JOIN sys_user u ON ta.grid_man_id = u.id " +
            "GROUP BY u.grid_province")
    List<Map<String, Object>> selectProvinceStatistics();

    /**
     * AQI指数分布统计 - 按等级分组
     */
    @Select("SELECT " +
            "  CASE " +
            "    WHEN ad.final_aqi <= 50 THEN 1 " +
            "    WHEN ad.final_aqi <= 100 THEN 2 " +
            "    WHEN ad.final_aqi <= 150 THEN 3 " +
            "    WHEN ad.final_aqi <= 200 THEN 4 " +
            "    WHEN ad.final_aqi <= 300 THEN 5 " +
            "    ELSE 6 END as aqi_level, " +
            "  COUNT(*) as count " +
            "FROM aqi_data ad " +
            "WHERE ad.is_exceeded = 1 " +
            "GROUP BY aqi_level " +
            "ORDER BY aqi_level")
    List<Map<String, Object>> selectLevelDistribution();

    /**
     * AQI指数趋势统计 - 近12个月每月超标数
     */
    @Select("SELECT " +
            "  DATE_FORMAT(ad.create_time, '%Y-%m') as stat_month, " +
            "  COUNT(*) as total_count, " +
            "  SUM(CASE WHEN ad.is_exceeded = 1 THEN 1 ELSE 0 END) as exceeded_count " +
            "FROM aqi_data ad " +
            "WHERE ad.create_time >= DATE_SUB(NOW(), INTERVAL 12 MONTH) " +
            "GROUP BY DATE_FORMAT(ad.create_time, '%Y-%m') " +
            "ORDER BY stat_month")
    List<Map<String, Object>> selectMonthlyTrend();

    /**
     * 实时统计 - 总数、良好数、超标数
     */
    @Select("SELECT " +
            "  COUNT(*) as total_count, " +
            "  SUM(CASE WHEN ad.is_exceeded = 0 THEN 1 ELSE 0 END) as good_count, " +
            "  SUM(CASE WHEN ad.is_exceeded = 1 THEN 1 ELSE 0 END) as bad_count " +
            "FROM aqi_data ad")
    Map<String, Object> selectRealtimeStatistics();
}
