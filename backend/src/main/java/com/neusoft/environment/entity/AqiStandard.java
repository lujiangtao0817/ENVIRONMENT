package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * AQI标准参考实体
 */
@Data
@TableName("aqi_standard")
public class AqiStandard {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** AQI等级(1-6) */
    private Integer aqiLevel;

    /** 等级名称 */
    private String levelName;

    /** AQI范围下限 */
    private Integer aqiMin;

    /** AQI范围上限 */
    private Integer aqiMax;

    /** PM2.5浓度限值 */
    private BigDecimal pm25Limit;

    /** SO2浓度限值 */
    private BigDecimal so2Limit;

    /** CO浓度限值 */
    private BigDecimal coLimit;
}
