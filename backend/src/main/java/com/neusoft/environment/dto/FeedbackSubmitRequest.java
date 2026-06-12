package com.neusoft.environment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 提交监督反馈请求
 */
@Data
public class FeedbackSubmitRequest {

    @NotBlank(message = "省不能为空")
    private String province;

    @NotBlank(message = "市不能为空")
    private String city;

    /** 具体地址 */
    private String addressDetail;

    @NotNull(message = "预估AQI等级不能为空")
    private Integer estimatedAqiLevel;

    /** 空气质量描述信息 */
    private String description;
}
