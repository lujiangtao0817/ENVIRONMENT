package com.neusoft.environment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.environment.dto.LoginRequest;
import com.neusoft.environment.dto.RegisterRequest;
import com.neusoft.environment.entity.SysUser;
import com.neusoft.environment.vo.LoginVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<SysUser> {

    /**
     * 注册（仅限公众监督员）
     */
    void register(RegisterRequest request);

    /**
     * 登录
     */
    LoginVO login(LoginRequest request);

    /**
     * 根据登录编码查找用户
     */
    SysUser findByLoginCode(String loginCode);

    /**
     * 根据手机号查找用户
     */
    SysUser findByPhone(String phone);
}
