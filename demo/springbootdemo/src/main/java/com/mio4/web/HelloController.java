package com.mio4.web;

import com.mio4.pojo.User;
import com.mio4.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;


@Slf4j
@Controller
public class HelloController {

//    @Autowired
//    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @GetMapping("hello")
    @ResponseBody
    public String hello(){
//        System.out.println("hello there~~~");
        log.debug("Hello method is running");
        return "HELLO";
    }


    @GetMapping("/user/{id}")
    @ResponseBody
    public User hello(@PathVariable("id") Long id){
        return userService.queryById(id);
    }
}
