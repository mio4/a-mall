package com.leyou.user.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodeUtils;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        msg.put("phone",phone);
        msg.put("code",code);

        //发送验证码
        //TODO 修复发送短信接口&购买短信服务
//        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);
        log.error("[发送的验证码]:{}",code);

        //保存到Redis
        stringRedisTemplate.opsForValue().set(key,code,5,TimeUnit.MINUTES);
    }


    public void register(User user, String code) throws LyException {
        //校验验证码
        String cache_code = stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        log.error("[从Redis中获取的验证码]:{}",cache_code);
        if(!StringUtils.equals(cache_code,code)){
            log.error("[用户注册]注册失败，验证码1:{}，验证码2：{}",cache_code,code);
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

    public User queryUserByUsernameAndPassword(String username, String password) throws LyException {
        User user = new User();
        user.setUsername(username);
        User record = userMapper.selectOne(user);
        // 校验用户名
        if(record == null){
//            throw new LyException(ExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
            return null;
        }
        //校验密码-使用salt
        String salt = record.getSalt();
        String generate_password = CodeUtils.md5Hex(password,salt);
        if(!StringUtils.equals(record.getPassword(),generate_password)){
//            throw new LyException(ExceptionEnum.INVALID_USERNAME_OR_PASSWORD);
            return null;
        }

        //用户名和密码校验正确
        return record;
    }
}
