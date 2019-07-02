package com.leyou.auth;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "E:/GitHub/a-mall/local/rsa.pub";
    private static final String priKeyPath = "E:/GitHub/a-mall/local/rsa.pri";

    private PublicKey publicKey;
    private PrivateKey privateKey;

    private String token;


    /**
     * 测试RSA加密算法
     * @throws Exception
     */
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath,priKeyPath,"mio4");
    }


    /**
     * 首先从文件中获取生成的公钥和私钥
     * @throws Exception
     */
    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 测试生成token
     */
    @Test
    public void testGenerateToken() throws Exception {
        UserInfo userInfo = new UserInfo(20L,"mio");
        token = JwtUtils.generateToken(userInfo,privateKey,5);
        System.out.println("token = " + token);
    }

    /**
     * 测试解析token
     */
    @After
    public void testParseToken() throws Exception {
//        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoibWlvIiwiZXhwIjoxNTYyMDgxMjA3fQ.aGRAnOD5GoEkUj7dM74H64ajNL6KBJeeWAXXBYWLeTdqfIoiILk5xeae4Z8BpFLP03iKNVhjZTQrrsUcsQgiVlx9wWHocCHduCnF2XvPYmdur5pUAJZxWTHJGPqbRBbW6bCFAXr2j6AvVxY-fFQNc_1eB6xC0F0BJW1Da3e8UZk";

        UserInfo userInfo = JwtUtils.getInfoFromToken(token,publicKey);

        System.out.println("user id = " + userInfo.getId());
        System.out.println("username = " + userInfo.getUsername());
    }

}
