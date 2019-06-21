package com.leyou.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 解决跨域问题的过滤器
 */
@Configuration
public class GlobalCorsConfig {

    private static final String domain = "http://manage.leyou.com";
    private static final String domain2 = "http://www.leyou.com";

    @Bean
    public CorsFilter corsFilter() {
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //  1) 允许的域,不要写*，否则cookie就无法使用了
        config.addAllowedOrigin(domain);
        config.addAllowedOrigin(domain2);
        //  2) 是否发送Cookie信息
        config.setAllowCredentials(true);
        //  3) 允许的请求方式
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        //  4）允许的头信息
        config.addAllowedHeader("*");
        //  5) 权限有效时长
        config.setMaxAge(3600L);

        //2.添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //3.返回新的CorsFilter.
        return new CorsFilter(configSource);
    }
}