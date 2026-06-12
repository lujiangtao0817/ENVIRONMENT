package com.neusoft.environment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体
 */
@Data
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 手机号（监督员唯一标识） */
    private String phone;

    /** 登录编码（网格员/管理员/决策者用） */
    private String loginCode;

    /** 加密密码 */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 年龄 */
    private Integer age;

    /** 性别 0女 1男 */
    private Integer gender;

    /** 角色 0公众监督员 1网格员 2管理员 3决策者 */
    private Integer role;

    /** 所属省（网格员用） */
    private String gridProvince;

    /** 所属市（网格员用） */
    private String gridCity;

    /** 工作状态 0空闲 1忙碌（网格员用） */
    private Integer workStatus;

    /** 账号状态 0禁用 1正常 */
    private Integer accountStatus;

    /** 最近登录时间 */
    private Date lastLoginTime;

    /** 创建时间 */
    private Date createTime;
}
