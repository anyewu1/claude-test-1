package com.jd.controller;

import com.jd.common.Result;
import com.jd.common.UserContext;
import com.jd.dto.LoginRequest;
import com.jd.dto.RegisterRequest;
import com.jd.entity.User;
import com.jd.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterRequest req) {
        return Result.success("注册成功", userService.register(req));
    }

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginRequest req) {
        return Result.success("登录成功", userService.login(req));
    }

    @GetMapping("/profile")
    public Result<?> getProfile() {
        return Result.success(userService.getProfile(UserContext.getUserId()));
    }

    @PutMapping("/profile")
    public Result<?> updateProfile(@RequestBody User user) {
        userService.updateProfile(UserContext.getUserId(), user);
        return Result.success("更新成功", null);
    }
}
