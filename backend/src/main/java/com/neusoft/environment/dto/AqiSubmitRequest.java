package com.neusoft.environment.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 提交AQI实测数据请求
 */
@Data
public class AqiSubmitRequest {

    @NotNull(message = "反馈任务ID不能为空")
    private Long feedbackId;

    @NotNull(message = "SO2 AQI浓度值不能为空")
    private BigDecimal so2Aqi;

    @NotNull(message = "CO AQI浓度值不能为空")
    private BigDecimal coAqi;

    @NotNull(message = "PM2.5 AQI浓度值不能为空")
    private BigDecimal pm25Aqi;
}
