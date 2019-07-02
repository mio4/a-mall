package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "user:verify:phone:";
    private static final Integer LIVE_TIME = 5; //验证码存活时间，单位为分

    public Boolean checkData(String data, Integer type) throws LyException {
        User user = new User();
        //判断数据类型
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        int count = userMapper.selectCount(user);
        return count == 0;
    }

    public void sendCode(String phone) {
        //生成Redis Key
        String key = KEY_PREFIX + phone;
        //生成验证码
        String code = NumberUtils.generateCode(6);

        Map<String,String> msg = new HashMap<>();
        msg.put("phone","18011194449");
        msg.put("code",code);

        //发送验证码
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);

        //保存到Redis
        stringRedisTemplate.opsForValue().set(key,code,5,TimeUnit.MINUTES);
    }


    public void register(User user, String code) throws LyException {
        //TODO 后端数据校验-用户名、手机号、账号、密码


        //校验验证码
        String cache_code = stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if(!StringUtils.equals(cache_code,code)){
            throw new LyException(ExceptionEnum.INVALID_VERIFY_CODE);
        }
        //密码加密
        String salt = CodeUtils.generateSalt();
        user.setPassword(CodeUtils.md5Hex(user.getPassword(),salt));
        user.setSalt(salt);
        user.setCreated(new Date());
        //写入数据库
        userMapper.insert(user);
    }
}
