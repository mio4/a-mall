package com.mio4.service;

import com.mio4.mapper.UserMapper;
import com.mio4.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询id对应的User
     * @param id
     * @return
     */
    public User queryById(Long id){
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * CREATE——添加事务支持
     * @param user
     */
    @Transactional
    public void inserUser(User user){
        userMapper.insert(user);
    }
}
