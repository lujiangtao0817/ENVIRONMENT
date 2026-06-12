package com.neusoft.environment.controller;

import com.neusoft.environment.dto.LoginRequest;
import com.neusoft.environment.dto.RegisterRequest;
import com.neusoft.environment.service.UserService;
import com.neusoft.environment.vo.LoginVO;
import com.neusoft.environment.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 认证控制器（公开接口）
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    /**
     * 注册（仅限公众监督员）
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResultVO<?> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResultVO.success("注册成功");
    }

    /**
     * 登录
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResultVO<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        LoginVO vo = userService.login(request);
        return ResultVO.success("登录成功", vo);
    }
}
