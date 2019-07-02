package com.leyou.user.controller;

import com.leyou.common.exception.LyException;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 校验用户数据，主要包括对于手机号码、用户名等唯一性
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data") String data,
                                             @PathVariable("type") Integer type) throws LyException {

        return ResponseEntity.ok(userService.checkData(data,type));
    }

    /**
     * 根据用户输入的手机号码，生成随机验证码，发送短信
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Boolean> sendCode(@RequestParam("phone") String phone){
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 用户注册
     * @param user 接收username、password、phone
     * @param code 短信验证码
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@Valid User user,
                                            @RequestParam("code") String code) throws LyException {

        userService.register(user,code);
        //Build the response entity with no body.
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * TODO maybe bug
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(@RequestParam("username") String username,
                                                               @RequestParam("password") String password) throws LyException {
        return ResponseEntity.ok(userService.queryUserByUsernameAndPassword(username,password));
    }






}
