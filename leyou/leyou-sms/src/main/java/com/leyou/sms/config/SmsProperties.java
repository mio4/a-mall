package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 将application.yml中属性注入到SmsProperties类
 */
@Data
@Component
@ConfigurationProperties(prefix = "leyou.sms")
public class SmsProperties {

    private String tpl_id;

    private String key;
}
