package com.taopiaopiao.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * CORS跨域过滤器
 */
public class CorsFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(CorsFilter.class);
    
    // 允许的域名列表（生产环境应从配置文件读取）
    private static final Set<String> ALLOWED_ORIGINS = new HashSet<>(Arrays.asList(
        "http://localhost:8080",
        "http://localhost:3000",
        "http://localhost:5173",
        "http://127.0.0.1:8080",
        "http://127.0.0.1:3000",
        "http://127.0.0.1:5173"
    ));
    
    // 允许的请求方法
    private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    
    // 允许的请求头
    private static final String ALLOWED_HEADERS = "Content-Type, Authorization, X-Requested-With";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("CORS过滤器初始化完成");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String origin = httpRequest.getHeader("Origin");
        
        // 验证Origin是否在允许列表中
        if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        }
        
        httpResponse.setHeader("Access-Control-Allow-Methods", ALLOWED_METHODS);
        httpResponse.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Expose-Headers", "Authorization");

        // 处理预检请求
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("CORS过滤器销毁");
    }
}
