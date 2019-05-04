package com.mio4.consumer.client;

import com.mio4.consumer.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class UserClientImpl implements UserClient {
    @Override
    public User queryById(Long id) {
        User user = new User();
        user.setName("fake name");
        return user;
    }
}
