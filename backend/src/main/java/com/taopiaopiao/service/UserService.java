package com.taopiaopiao.service;

import com.taopiaopiao.entity.User;
import com.taopiaopiao.exception.BusinessException;
import com.taopiaopiao.mapper.UserMapper;
import com.taopiaopiao.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务类
 */
@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 用户注册
     */
    public User register(String username, String password, String nickname) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(username) != null) {
            throw new BusinessException("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(username);
        // 使用BCrypt加密密码
        user.setPassword(PasswordUtil.encrypt(password));
        user.setNickname(nickname != null ? nickname : username);
        user.setAvatar("/images/default-avatar.svg");
        
        userMapper.insert(user);
        logger.info("用户注册成功: username={}, userId={}", username, user.getId());
        user.setPassword(null);
        return user;
    }
    
    /**
     * 用户登录
     */
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            logger.warn("登录失败，用户不存在: {}", username);
            throw new BusinessException("用户名或密码错误");
        }
        // 使用BCrypt验证密码
        if (!PasswordUtil.verify(password, user.getPassword())) {
            logger.warn("登录失败，密码错误: {}", username);
            throw new BusinessException("用户名或密码错误");
        }
        user.setPassword(null);
        return user;
    }
    
    /**
     * 根据ID获取用户
     */
    public User getById(Long id) {
        User user = userMapper.findById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
    
    /**
     * 更新用户信息
     */
    public User update(User user) {
        // 不允许通过此方法更新密码
        user.setPassword(null);
        userMapper.update(user);
        logger.info("用户信息更新: userId={}", user.getId());
        return getById(user.getId());
    }
}
