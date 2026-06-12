package com.neusoft.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.environment.dto.LoginRequest;
import com.neusoft.environment.dto.RegisterRequest;
import com.neusoft.environment.entity.SysUser;
import com.neusoft.environment.mapper.UserMapper;
import com.neusoft.environment.service.UserService;
import com.neusoft.environment.utils.JwtUtils;
import com.neusoft.environment.utils.PasswordUtils;
import com.neusoft.environment.vo.LoginVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements UserService {

    @Resource
    private PasswordUtils passwordUtils;

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public void register(RegisterRequest request) {
        // 检查手机号是否已注册
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, request.getPhone());
        if (this.count(wrapper) > 0) {
            throw new IllegalArgumentException("该手机号已注册");
        }

        SysUser user = new SysUser();
        user.setPhone(request.getPhone());
        user.setPassword(passwordUtils.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setAge(request.getAge());
        user.setGender(request.getGender());
        user.setRole(0); // 0 = 公众监督员
        this.save(user);
    }

    @Override
    public LoginVO login(LoginRequest request) {
        SysUser user;
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        switch (request.getRole()) {
            case 0: // 公众监督员 - 手机号登录
                user = findByPhone(request.getPhone());
                break;
            case 1: // 网格员 - 登录编码登录
                user = findByLoginCode(request.getLoginCode());
                break;
            case 2: // 系统管理员 - 登录编码登录
                user = findByLoginCode(request.getLoginCode());
                break;
            case 3: // 决策者 - 登录编码登录
                user = findByLoginCode(request.getLoginCode());
                break;
            default:
                throw new IllegalArgumentException("无效的角色类型");
        }

        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        if (!user.getRole().equals(request.getRole())) {
            throw new IllegalArgumentException("角色不匹配");
        }

        if (!passwordUtils.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }

        // 生成 JWT Token
        String phoneOrCode = user.getPhone() != null ? user.getPhone() : user.getLoginCode();
        String token = jwtUtils.generateToken(
                user.getId(),
                phoneOrCode,
                user.getRealName(),
                user.getRole()
        );

        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .realName(user.getRealName())
                .role(user.getRole())
                .build();
    }

    @Override
    public SysUser findByLoginCode(String loginCode) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getLoginCode, loginCode);
        return this.getOne(wrapper);
    }

    @Override
    public SysUser findByPhone(String phone) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, phone);
        return this.getOne(wrapper);
    }
}
