package com.englishtest.englishtest.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class FrameOptionsInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Đặt header cho phép iframe từ cùng domain
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        return true;
    }
}
