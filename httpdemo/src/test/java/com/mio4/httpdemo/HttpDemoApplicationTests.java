package com.mio4.httpdemo;

import com.mio4.httpdemo.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HttpDemoApplication.class)
public class HttpDemoApplicationTests {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void httpGet(){
        User user = restTemplate.getForObject("http://localhost:8899/user/13",User.class);
        System.out.println(user);
    }
}
