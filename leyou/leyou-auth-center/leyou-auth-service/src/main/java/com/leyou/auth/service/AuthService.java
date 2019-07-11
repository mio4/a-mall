package com.leyou.auth.service;

import com.leyou.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    public String login(String username, String password) throws LyException {
        //校验用户名和密码
        User user = userClient.queryUserByUsernameAndPassword(username, password);
        if(user == null){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
        }
        //生成token
        try {
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            return token;
        } catch (Exception e) {
            log.error("[授权中心]生成token失败，用户名称:{}",username);
            throw new LyException(ExceptionEnum.CREATE_TOKEN_FAILED);
        }
    }
}
