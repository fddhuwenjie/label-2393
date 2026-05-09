package com.taopiaopiao.dto;

import com.taopiaopiao.entity.User;
import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {
    private String token;
    private User user;
    
    public static LoginResponse of(String token, User user) {
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUser(user);
        return response;
    }
}
