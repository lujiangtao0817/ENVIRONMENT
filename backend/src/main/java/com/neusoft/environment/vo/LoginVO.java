package com.neusoft.environment.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 登录响应
 */
@Data
@Builder
public class LoginVO {

    /** JWT Token */
    private String token;

    /** 用户ID */
    private Long userId;

    /** 真实姓名 */
    private String realName;

    /** 角色 */
    private Integer role;
}
