package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * CREATE TABLE `tb_user` (
 *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
 *   `username` varchar(50) NOT NULL COMMENT '用户名',
 *   `password` varchar(32) NOT NULL COMMENT '密码，加密存储',
 *   `phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
 *   `created` datetime NOT NULL COMMENT '创建时间',
 *   `salt` varchar(32) NOT NULL COMMENT '密码加密的salt值',
 *   PRIMARY KEY (`id`),
 *   UNIQUE KEY `username` (`username`) USING BTREE
 * ) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='用户表';
 */

@Data
@Table(name = "tb_user")
public class User {

    //TODO what do this mean?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4,max = 30, message = "用户名长度必须在4~32位之间")
    private String username;

    @Length(min = 4,max = 30, message = "密码长度必须在4~32位之间")
    @JsonIgnore
    private String password; //密码

    @Pattern(regexp = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$",message = "手机号非法")
    private String phone;

    private Date created;

    @JsonIgnore
    private String salt; //盐值

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", created=" + created +
                ", salt='" + salt + '\'' +
                '}';
    }
}
