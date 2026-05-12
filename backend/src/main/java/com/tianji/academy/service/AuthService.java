package com.tianji.academy.service;

import com.tianji.academy.dto.LoginRequest;
import com.tianji.academy.dto.LoginResponse;
import com.tianji.academy.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
}
