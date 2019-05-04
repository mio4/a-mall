package com.mio4.consumer.controller;

import com.mio4.consumer.client.UserClient;
import com.mio4.consumer.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("consumer")
@DefaultProperties(defaultFallback = "queryByFallback")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private UserClient userClient;
//    private RibbonLoadBalancerClient client;

//    @HystrixCommand(commandProperties = {
////            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "4000"),
//            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),
//            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),
//    })
//    @GetMapping("/{id}")
//    public String queryById(@PathVariable("id") Long id){
//        //获取实例
////        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
//        //从实例中获取端口和IP
////        ServiceInstance instance = instances.get(0);
////        String url = "http://" + instance.getHost()  + ":" + instance.getPort() + "/user/" + id;
////        String url = "http://localhost:8081/user/" + id;
//
////        ServiceInstance instance = client.choose("user-service");
////        if(id%2 == 0){
////            throw new RuntimeException("make something wrong");
////        }
////
////        String url = "http://user-service/user/" + id;
////        String user = restTemplate.getForObject(url,String.class);
////        return user;
//    }

    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id){
        return userClient.queryById(id);
    }

//    public String queryByFallback(Long id){
//        String errMsg = "服务器挂了";
//        return errMsg;
//    }
//
//    public String queryByFallback(){
//        String errMsg = "服务器挂了";
//        return errMsg;
//    }
}
