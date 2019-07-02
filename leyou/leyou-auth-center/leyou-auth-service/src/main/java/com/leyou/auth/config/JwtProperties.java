package com.leyou.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {

    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private Long expire;

    //类加载之后，读取公钥和私钥
}
