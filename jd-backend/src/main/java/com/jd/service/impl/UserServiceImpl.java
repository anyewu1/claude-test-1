package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.dto.LoginRequest;
import com.jd.dto.RegisterRequest;
import com.jd.entity.User;
import com.jd.mapper.UserMapper;
import com.jd.service.UserService;
import com.jd.util.JwtUtil;
import com.jd.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtUtil jwtUtil;
    private final PasswordUtil passwordUtil;

    @Override
    public Map<String, Object> register(RegisterRequest req) {
        if (count(new LambdaQueryWrapper<User>().eq(User::getUsername, req.getUsername())) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordUtil.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        user.setAvatar("https://picsum.photos/seed/" + req.getUsername() + "/100/100");
        user.setRole("USER");
        save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        return result;
    }

    @Override
    public Map<String, Object> login(LoginRequest req) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, req.getUsername()));
        if (user == null || !passwordUtil.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("avatar", user.getAvatar());
        result.put("role", user.getRole());
        return result;
    }

    @Override
    public User getProfile(Long userId) {
        return getById(userId);
    }

    @Override
    public void updateProfile(Long userId, User user) {
        user.setId(userId);
        user.setUsername(null);
        user.setPassword(null);
        user.setRole(null);
        updateById(user);
    }
}
