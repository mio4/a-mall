package com.leyou.auth.controller;

import com.leyou.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return 返回授权中心给的token
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password){
        String token = authService.login(username,password);
        return ResponseEntity.ok(token);
    }



}
