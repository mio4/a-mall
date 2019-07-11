package com.leyou.auth.controller;

import com.leyou.auth.service.AuthService;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${leyou.jwt.cookieName}")
    private String cookieName;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return 返回授权中心给的token
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws LyException {
        String token = authService.login(username,password);
        //token写入token
//        Cookie cookie = new Cookie(cookieName, token);
//        cookie.setMaxAge(60*60); //设置最大存活时间
//        cookie.setHttpOnly(true); //不允许JS操作此Cookie
//        response.addCookie(cookie);
        CookieUtils.setCookie(request,response,cookieName,token,60*60,true);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
