package com.neusoft.environment.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 指派网格员请求
 */
@Data
public class AssignRequest {

    @NotNull(message = "反馈ID不能为空")
    private Long feedbackId;

    @NotNull(message = "网格员ID不能为空")
    private Long gridManId;
}
