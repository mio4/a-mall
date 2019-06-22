package com.leyou.page.web;

import com.leyou.page.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String toHello(Model model){
        model.addAttribute("msg","this is a message");

        User user = new User();
        user.setAge(20);
        user.setName("mio");
        user.setFriend(new User("a friend",21,null));

        model.addAttribute("user",user);

        return "hello"; //"hello"是视图名称
    }
}
