package com.mio4.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
//@EnableConfigurationProperties(JdbcProperties.class)
//@PropertySource("classpath:jdbc.properties")
public class JdbcConfig {

//    @Value("${jdbc.url}")
//    private String url;
//
//    @Value("${jdbc.driverClassName}")
//    private String driverClassName;
//
//    @Value("${jdbc.username}")
//    private String username;
//
//    @Value("${jdbc.password}")
//    private String password;

//    @Bean
//    public DataSource dataSource(){
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//    }

//    @Bean
//    public DataSource dataSource(JdbcProperties prop){
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName(prop.getDriverClassName());
//        dataSource.setUrl(prop.getUrl());
//        dataSource.setUsername(prop.getUsername());
//        dataSource.setPassword(prop.getPassword());
//        return dataSource;
//    }

//
//    @Bean
//    @ConfigurationProperties(prefix = "jdbc")
//    public DataSource dataSource(){
//        DruidDataSource dataSource = new DruidDataSource();
//        return dataSource;
//    }

}
