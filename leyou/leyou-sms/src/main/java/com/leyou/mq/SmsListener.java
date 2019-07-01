package com.leyou.mq;

import com.leyou.sms.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Slf4j
@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    /**
     * 发送短信验证码
     * @param msg
     *          phone:phoneNumber
     *          code:短信验证码
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "sms_verify_code_queue",durable = "true"),
            exchange = @Exchange(name = "ly.sms.exchange",type = ExchangeTypes.TOPIC),
            key = {"sms.verify.code"}
    ))
    public void listenInsertOrUpdate(Map<String,String> msg){
        if(CollectionUtils.isEmpty(msg)){
            return;
        }
        String phone = msg.get("phone");
        if(StringUtils.isBlank(phone)){
            return;
        }
        String code = msg.get("code");
        if(StringUtils.isBlank(code)){
            return;
        }
        //发送短信
        smsUtil.sendSms(phone,code);
//        SmsUtil smsUtil = new SmsUtil();
//        smsUtil.test();

        //记录日志
//        log.info("[短信服务], 发送短信验证码，手机号:{}",phone);
    }
}
