package com.neusoft.environment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册请求（仅限公众监督员）
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @NotNull(message = "年龄不能为空")
    private Integer age;

    @NotNull(message = "性别不能为空")
    private Integer gender;
}
