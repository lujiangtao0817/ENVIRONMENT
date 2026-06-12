package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * AQI统计实体
 */
@Data
@TableName("aqi_statistics")
public class AqiStatistics {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 统计月份 YYYY-MM */
    private String statMonth;

    /** 省 */
    private String province;

    /** AQI等级 */
    private Integer aqiLevel;

    /** SO2超标累计数 */
    private Integer so2ExceededCount;

    /** CO超标累计数 */
    private Integer coExceededCount;

    /** PM2.5超标累计数 */
    private Integer pm25ExceededCount;

    /** AQI超标累计数 */
    private Integer aqiExceededCount;

    /** 检测总数 */
    private Integer totalDetectCount;

    /** 检测良好数 */
    private Integer goodDetectCount;

    /** 检测超标数 */
    private Integer badDetectCount;
}
