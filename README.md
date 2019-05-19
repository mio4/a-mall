目的：通过从零开始搭建一个B2C电商项目学习前沿技术的基础知识

## 学习进度

Update Time : 2019/5/18

1. SpringBoot自动配置原理——yaml文件配置
2. SpringMVC——拦截器配置、Slf4j日志、通用Mapper配置、注解复习
3. 系统架构：集中式架构——>SOA架构——>微服务架构（RPC | HTTP）
4. Spring访问Rest服务的客户端——RestTemplate
5. SpringCloud入门——基于SpringBoot的微服务容器
    父子工程——服务提供方&服务接收方
6. Eureka服务管理中心——能够管理消费者-生产者模型：pom.xml中添加maven依赖，添加@EnableDiscoveryClient注解，配置application.yml文件——改进：Consumer动态获取Service地址——搭建Eureka中心集群（eg：两个eureka-server，两个eureka-client）
7. Eureka——Ribbon负载均衡 | 对于Ribbon源码的分析：轮询算法&随机算法
8. Histrix熔断器——设置最长等待时间，定义错误情况下处理函数
9. Feign——对Rest请求进行隐藏，假扮在访问本地服务
10. Zuul——使用微服务网关：（1）控制所有微服务路由 （2）过滤器鉴权和限流（3）自定义过滤器
11. Vue基础知识
12. FastDFS
13. Vuetify：Vue框架
14. Element-Ui：饿了么前端框架
15. ElasticSearch

# 项目进度

## 前端

- [ ] manage.leyou.com
- [x] IP访问——>二级域名访问

## 后台

- [x] 搭建基础架构
- [x] 实现工具类，通用异常处理类
- [x] 虚拟机CentOS的Nginx代理请求
- [x] 实现FastDFS分布式文件上传功能（配置Ngnix+FastDFS-module）
- [ ] 设计商品表结构
- [ ] 购物车功能（复习Cookie和Session）

# 笔记

```cmd
# 运行vue项目
npm run dev 
# windows host文件
C:\Windows\System32\drivers\etc\hosts
# switchHosts
# Nginx
E:\nginx-1.14.2\conf\nginx.conf
```

###  Windows下Nginx配置

```
	./nginx.exe
	./nginx.exe -s reload
	./nginx.exe -s quit
```

### mysql导入sql脚本

```cmd
# mysql 安装路径
C:\Program Files\MySQL\MySQL Server 5.5

drop database amall;
create database amall character set utf8;
use amall;
source ‪E:\GitHub\a-mall\info\amall.sql;
```

### 设计表结构

```cmd

```

### 前后台交互API

```cmd
http://localhost:8081/category/list?pid=0 # 直接访问
http://localhost:10010/api/category/list?pid=0 # 从网关转发
```

### FastDFS

```cmd
本机IP ：
虚拟机IP：
VMware NetWork Adapter VMNet1
192.168.211.1
VMware NetWork Adapter VMNet8
192.168.92.1
无线局域网适配器WLAN
10.135.93.200 | 10.136.51.13
Ubuntu虚拟机
192.168.92.128
Ubuntu共享文件夹
计算机-> mnt/hgfs/Ubuntu Virtual Share
E:\Ubuntu Virtual Share
```



```cmd
# 相关配置
# 防火墙
service iptables status | service iptables off
chkconfig iptables off
# Centos IP
192.168.92.129
# 本机IP——动态更新
10.136.51.13
10.135.51.174
10.135.51.174
# FastDFS
/etc/fdfs ——配置文件
/etc/init.d/fdfs_trackerd ——启动文件
service fdfs_trackerd start ——启动服务
# IP变更之后需要的fix
本机IP和CentOS虚拟机IP可能是动态IP，需要更改的配置：
1. 本机IP改变
	(1)CentOS /opt/nginx/conf/nginx.conf中proxy_pass 修改为新的本机IP
	# vim替换批处理脚本
	
2. CentOS IP改变
	(1)FastDFS中tracker storage配置文件：
		/etc/fdfs/storage.conf —— tracker_server修改为新的虚拟机IP
		/etc/fdfs/tracker.conf —— 不需要修改（可以配置新的端口）
		/etc/fdfs/client.conf  —— tracker_server修改为新的虚拟机IP
	(2)SwitchHost.exe中修改hosts映射文件	
```

![1557711001551](C:\Users\mio\AppData\Roaming\Typora\typora-user-images\1557711001551.png)

# API

## manage.leyou.com

| 功能 | 请求方法 | 请求路径  | 请求体 | 说明  | 响应体  | 说明|
| --- | ------- | -------- | ----- | ---- | ----- | ----- |
| 获取商品列表：需要后端分页 | GET  | /spu/page | key<br/>saleable<br/>page<br/>row<br/> | key：商品名称(模糊)，<br/>saleable：是否上架，<br/>page：当前页数，<br/>row：一页显示多少条 | {"total": 0,<br/>"totalPage": null,<br/>"items": [{<br/>"id": 129,<br/>"brandId": 18374,<br/>"cid1": 74,<br/>"cid2": 75,<br/>"cid3": 76,<br/>"title": "小米（MI） 红米5 plus 手机 （更新）",<br/>"subTitle": "18:9全面屏，4000mAh大电池，骁龙八核处理器",<br/>"saleable": true,<br/>"valid": true,<br/>"createTime": "2018-04-21T07:59:38.000+0000",<br/>"cname": "手机/手机通讯/手机",<br/>"bname": "小米（MI）"}]<br/>}
} | total：商品总条数<br/>totalPage：总共多少页<br/>items：商品列表，每个item包含商品的详细信息 |




| 功能                       | 请求方法 | 请求路径 | 请求体 | 说明 | 响应体 | 说明 |
| -------------------------- | -------- | -------- | ------ | ---- | ------ | ---- |
| 获取商品列表：需要后端分页 |          |          |        |      |        |                                                            |

| 获取商品列表：需要后端分页     | GET      | /spu/page   | key<br>saleable<br>page<br>row | key：商品名称(模糊)，<br>saleable：是否上架，<br/>page：当前页数，<br/>row：一页显示多少条 | {
<br/>	"total": 0,<br>
	"totalPage": null,<br/>
	"items": [{<br/>
		"id": 129,<br/>
		"brandId": 18374,<br/>
		"cid1": 74,<br/>
		"cid2": 75,<br/>
		"cid3": 76,<br/>
		"title": "小米（MI） 红米5 plus 手机 （更新）",<br/>
		"subTitle": "18:9全面屏，4000mAh大电池，骁龙八核处理器",<br/>
		"saleable": true,<br/>
		"valid": true,<br/>
		"createTime": "2018-04-21T07:59:38.000+0000",<br/>
		"cname": "手机/手机通讯/手机",<br/>
		"bname": "小米（MI）"}]<br>}<br/> | total：商品总条数<br/>totalPage：总共多少页
items：商品列表，每个item包含商品的详细信息 |

|      |      |      |      |      |      |      |
| ---- | ---- | ---- | ---- | ---- | ---- | ---- |
|      |      |      |      |      |      |      |
| 新增商品：需要填写商品详细信息 | POST     | /item/goods |                                | { <br/>  "brandId": 8557,
     "title": "华为超级P",
  "subTitle": "不知道写啥",  "spuDetail":[...],
     "skus": [{...}]
     "cid1":74,
     "cid2":75,
    "cid3":76
 } |                                                              |                                                              |





# 技术总结

## Java8

## Spring注解

# 项目中关注的一些细节

1. 项目是前后端分离的，从manage.leyou.com向api.leyou.com发起跨域请求是如何处理的？

    - [ ] JSONP （只能发送GET）
    - [ ] Nginx反向代理（需要配置nginx.conf）
    - [x] CORS（cross origin resource sharing）

2. 如何发送Ajax请求

    axios

3. 研发顺序

    数据库打表——>通过表构造JavaBean——>通用Mapper——>Service——>Controller——>前后端对接测试

4. 
















