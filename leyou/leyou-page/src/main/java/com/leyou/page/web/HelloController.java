package com.leyou.page.web;

import com.leyou.page.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String toHello(Model model){
        model.addAttribute("msg","this is a message");

        User user = new User();
        user.setAge(20);
        user.setName("mio");
        user.setFriend(new User("a friend",21,null));

        User user2 = new User("mio2",22,null);

        model.addAttribute("user",user);
        model.addAttribute("users", Arrays.asList(user,user2));



        return "hello"; //"hello"是视图名称
    }

}
