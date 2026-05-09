package com.taopiaopiao.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taopiaopiao.common.Result;
import com.taopiaopiao.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录认证拦截器
 */
public class AuthInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // OPTIONS请求直接放行（CORS预检）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        
        String token = extractToken(request);
        
        if (token == null || !JwtUtil.validateToken(token)) {
            logger.warn("未授权访问: {} {}", request.getMethod(), request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(
                Result.error(401, "请先登录")
            ));
            return false;
        }
        
        // 将用户ID存入request属性，供Controller使用
        Long userId = JwtUtil.getUserId(token);
        request.setAttribute("currentUserId", userId);
        request.setAttribute("currentUsername", JwtUtil.getUsername(token));
        
        return true;
    }
    
    /**
     * 从请求头提取Token
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
