package com.houyaozu.knowledge.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 设置跨域相关的HTTP头
        httpResponse.setHeader("Access-Control-Allow-Origin", "*"); // 允许所有来源
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        // 对于预检请求，直接返回
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
            return;
        }

        // 继续请求处理
        filterChain.doFilter(request, response);
    }
}
