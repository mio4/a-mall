package com.leyou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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

    private String username;

    @JsonIgnore
    private String password; //密码

    private String phone;

    private Date created;

    @JsonIgnore
    private String salt; //盐值

}
