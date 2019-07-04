package com.leyou.auth.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
@Data
@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {

    private String secret;
    private String pubKeyPath;
    private String priKeyPath;
    private int expire;
    private String cookieName;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    //类加载之后，读取公钥和私钥
    @PostConstruct
    public void init(){
        try {
            //判断公钥私钥是否存在
            File pubPath = new File(pubKeyPath);
            File priPath = new File(priKeyPath);
            if(!pubPath.exists() || !priPath.exists()){
                RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
            }
            //获取公钥私钥
            this.publicKey = RsaUtils.getPublicKey(this.pubKeyPath);
            this.privateKey = RsaUtils.getPrivateKey(this.priKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[生成公钥私钥失败]...");
        }
    }
}
