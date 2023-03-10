package com.example.javademo.mybatis.security.filter;

import com.example.javademo.mybatis.common.Result.ReturnCode;
import com.example.javademo.mybatis.security.Exception.CustomAuthenticationException;
import com.example.javademo.mybatis.security.config.WhiteUserList;
import com.example.javademo.mybatis.security.handler.LoginFailHandler;
import com.example.javademo.mybatis.security.jwt.JwtUtil;
import com.example.javademo.mybatis.security.service.CustomUserService;
import com.example.javademo.mybatis.utils.redis.ConfigRedis;
import com.example.javademo.mybatis.utils.redis.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
    private final CustomUserService customUserService;
    private final LoginFailHandler loginFailHandler;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String path = request.getRequestURI();
            ArrayList<String> whiteList = WhiteUserList.getWhiteUserList();
            if (whiteList.contains(path)) {
                doFilter(request, response, filterChain);
                return;
            }
            if (!(path.equals("/user/login"))) {
                try {
                    // ????????????????????????????????????????????????????????????????????????token???????????????????????????username
                    // 1. ??????????????????token
                    String token = request.getHeader("Authentication");

                    // 2 ?????????????????????token????????????
                    if (StringUtils.isEmpty(token)) {
                        throw new CustomAuthenticationException(ReturnCode.NoToken.getMessage());
                    }

                    // 5 ??????token??????username
                    String originUsername = JwtUtil.getUsernameFromToken(token);

                    if (StringUtils.isEmpty(originUsername)) {
                        throw new CustomAuthenticationException("444");
                    }
                    // 3 ??????token????????????
                    String targetToken = "token_" + originUsername;
                    String rightToken = (String)redisService.get(targetToken);
                    if (StringUtils.isEmpty(rightToken)) {
                        throw new CustomAuthenticationException("222");
                    }

                    // 4 token????????????
                    if (!rightToken.equals(token)) {
                        throw new CustomAuthenticationException("333");
                    }
                    // 1. ????????????????????????
                    UserDetails userDetails = customUserService.loadUserByUsername(originUsername);
                    System.out.println(userDetails);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    doFilter(request, response, filterChain);

                } catch (AuthenticationException e) {
                    if (path.equals("/user/logout")) {
                        doFilter(request, response, filterChain);
                    } else {
                        // ????????????????????????
                        loginFailHandler.onAuthenticationFailure(request, response, e);
                    }
                } catch (ExpiredJwtException e) {
                    doFilter(request, response, filterChain);
                }
            } else {
                // spring-security????????????context????????????????????????????????????????????????????????????
                // ???????????????
                doFilter(request, response, filterChain);
            }
    }
}
