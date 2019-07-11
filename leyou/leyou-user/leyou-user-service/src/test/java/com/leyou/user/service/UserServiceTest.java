package com.leyou.user.service;

import com.leyou.common.exception.LyException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testQueryUserByUsernameAndPassword(){
        String username = "mio4";
        String password = "mio4";
        try {
            userService.queryUserByUsernameAndPassword(username,password);
        } catch (LyException e) {
            e.printStackTrace();
        }
    }
}