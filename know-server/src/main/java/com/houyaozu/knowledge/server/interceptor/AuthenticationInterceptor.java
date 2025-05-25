package com.houyaozu.knowledge.server.interceptor;

import com.houyaozu.knowledge.common.exception.KnowledgeException;
import com.houyaozu.knowledge.common.login.LoginUser;
import com.houyaozu.knowledge.common.login.LoginUserHolder;
import com.houyaozu.knowledge.common.result.ResultCodeEnum;
import com.houyaozu.knowledge.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @ Author     ：侯耀祖
 * @ Description：
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null) {
                return true;
            }
            String token = authorization.substring("Bearer ".length());
            Claims claims = JwtUtil.parseToken(token);
            Integer userId = claims.get("userId", Integer.class);
            String username = claims.get("username", String.class);
            LoginUserHolder.setLoginUser(new LoginUser(userId, username));
        } catch (Exception e) {
            throw new KnowledgeException(ResultCodeEnum.TOKEN_EXPIRED);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserHolder.clear();
    }
}
