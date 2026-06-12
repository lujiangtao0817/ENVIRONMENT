package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * AQI实测数据实体
 */
@Data
@TableName("aqi_data")
public class AqiData {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 反馈ID */
    private Long feedbackId;

    /** 网格员ID */
    private Long gridManId;

    /** SO2 AQI浓度值 */
    private BigDecimal so2Aqi;

    /** CO AQI浓度值 */
    private BigDecimal coAqi;

    /** PM2.5 AQI浓度值 */
    private BigDecimal pm25Aqi;

    /** 最终AQI = MAX(SO2, CO, PM2.5) */
    private BigDecimal finalAqi;

    /** 是否超标 0否 1是 */
    private Integer isExceeded;

    /** 提交时间 */
    private Date createTime;
}
