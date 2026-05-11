package com.jd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.dto.LoginRequest;
import com.jd.dto.RegisterRequest;
import com.jd.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> register(RegisterRequest req);
    Map<String, Object> login(LoginRequest req);
    User getProfile(Long userId);
    void updateProfile(Long userId, User user);
}
