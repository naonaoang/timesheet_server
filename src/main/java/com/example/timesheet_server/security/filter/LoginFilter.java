package com.example.timesheet_server.security.filter;

import com.example.timesheet_server.constant.JwtConstant;
import com.example.timesheet_server.security.util.CookieUtil;
import com.example.timesheet_server.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String token = CookieUtil.getValue(req, JwtConstant.JWT_COOKIE_NAME);
        logger.info("+++++++++JwtFilter++++++++\n"+token);
        if (token!=null) {
            Integer userId = JwtUtil.getSubjectFromJwt(token);
            logger.info("Login with userID: "+userId);
        } else {
            String authLoginUrl = getFilterConfig().getInitParameter("authLoginUrl");
            res.sendRedirect(authLoginUrl + "?redirect=" + req.getRequestURL());
        }
    }
}