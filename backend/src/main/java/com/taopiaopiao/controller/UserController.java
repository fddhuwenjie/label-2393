package com.taopiaopiao.controller;

import com.taopiaopiao.common.Result;
import com.taopiaopiao.dto.LoginRequest;
import com.taopiaopiao.dto.LoginResponse;
import com.taopiaopiao.dto.RegisterRequest;
import com.taopiaopiao.entity.User;
import com.taopiaopiao.exception.ForbiddenException;
import com.taopiaopiao.service.UserService;
import com.taopiaopiao.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("用户注册: {}", request.getUsername());
        User user = userService.register(request.getUsername(), request.getPassword(), request.getNickname());
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        logger.info("用户注册成功: userId={}", user.getId());
        return Result.success(LoginResponse.of(token, user));
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("用户登录: {}", request.getUsername());
        User user = userService.login(request.getUsername(), request.getPassword());
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        logger.info("用户登录成功: userId={}", user.getId());
        return Result.success(LoginResponse.of(token, user));
    }
    
    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    public Result<User> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        User user = userService.getById(userId);
        return Result.success(user);
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }
    
    /**
     * 更新用户信息（需要登录，只能更新自己）
     */
    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @RequestBody User user, HttpServletRequest request) {
        Long currentUserId = (Long) request.getAttribute("currentUserId");
        
        // 权限校验：只能更新自己的信息
        if (!id.equals(currentUserId)) {
            throw new ForbiddenException("无权修改他人信息");
        }
        
        logger.info("用户更新信息: userId={}", id);
        user.setId(id);
        User updated = userService.update(user);
        return Result.success(updated);
    }
}
