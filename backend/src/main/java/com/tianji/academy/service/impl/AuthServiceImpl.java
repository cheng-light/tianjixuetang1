package com.tianji.academy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tianji.academy.dto.LoginRequest;
import com.tianji.academy.dto.LoginResponse;
import com.tianji.academy.dto.RegisterRequest;
import com.tianji.academy.entity.User;
import com.tianji.academy.exception.BusinessException;
import com.tianji.academy.mapper.UserMapper;
import com.tianji.academy.security.JwtTokenProvider;
import com.tianji.academy.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername()));
        if (user == null || user.getStatus() == 0 || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        List<String> roles = userMapper.selectRoleCodes(user.getId());
        String token = tokenProvider.createToken(user.getId(), user.getUsername(), roles);
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getNickname(), roles);
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        User exists = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername()));
        if (exists != null) {
            throw new BusinessException(400, "用户名已存在");
        }

        User emailExists = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail, request.getEmail()));
        if (emailExists != null) {
            throw new BusinessException(400, "邮箱已被使用");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(1);
        userMapper.insert(user);
        userMapper.insertDefaultStudentRole(user.getId());

        List<String> roles = userMapper.selectRoleCodes(user.getId());
        String token = tokenProvider.createToken(user.getId(), user.getUsername(), roles);
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getNickname(), roles);
    }
}
