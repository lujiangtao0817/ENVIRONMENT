package com.neusoft.environment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 登录请求
 */
@Data
public class LoginRequest {

    /** 角色：0公众监督员 1网格员 2管理员 3决策者 */
    @NotNull(message = "角色不能为空")
    private Integer role;

    /** 手机号（监督员登录用） */
    private String phone;

    /** 登录编码（网格员/管理员/决策者登录用） */
    private String loginCode;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
}
