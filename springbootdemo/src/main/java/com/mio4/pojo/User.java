package com.mio4.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

//lombok自动添加get/set方法
@Data
@Table(name = "tb_user")
public class User {

    //主键——自增
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String userName;

    private String password;

    private String name;

    private Integer age;

    private Date birthday;

    @Transient
    private Date created;
}
