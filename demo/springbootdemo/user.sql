-- 测试表
DROP TABLE `tb_user`;
CREATE TABLE `tb_user`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT  NULL COMMENT '年龄',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别：1.男性 2.女性',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY(`id`),
  UNIQUE KEY `username`(`user_name`)
)ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
-- 测试数据
insert into tb_user (user_name,age,sex) values ('mio',100,1);

