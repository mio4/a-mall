package com.mio4.user.service;

import com.mio4.user.mapper.UserMapper;
import com.mio4.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User queryById(Long id) {
//        try{
//            Thread.sleep(3000L);
//        }catch (Exception e){
//
//        }
        return this.userMapper.selectByPrimaryKey(id);
    }
}
