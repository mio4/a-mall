package com.mio4.consumer.controller;

import com.mio4.consumer.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id){
        //获取实例
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        //从实例中获取端口和IP
        ServiceInstance instance = instances.get(0);
        String url = "http://" + instance.getHost()  + ":" + instance.getPort() + "/user/" + id;
//        String url = "http://localhost:8081/user/" + id;

        User user = restTemplate.getForObject(url,User.class);
        return user;
    }
}
