-- 个人测试version
-- 规格参数组表
DROP TABLE  IF EXISTS `tb_spec_group`;
CREATE TABLE `tb_spec_group`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `cid` bigint(20) NOT NULL COMMENT '商品分类id，一个分类下有多个规格组',
    `name` varchar(50) NOT NULL COMMENT '规格组的名称',
    PRIMARY KEY (`id`),
    KEY `key_category` (`cid`)
)ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT '规格参数的分组表，每个商品下有多个规格参数组';
-- 插入测试数据
insert into tb_spec_group (cid,name) values(76,'主体');
insert into tb_spec_group (cid,name) values(76,'基本信息');
insert into tb_spec_group (cid,name) values(76,'操作系统');    
insert into tb_spec_group (cid,name) values(76,'主芯片');
insert into tb_spec_group (cid,name) values(76,'存储');
insert into tb_spec_group (cid,name) values(76,'摄像头');
insert into tb_spec_group (cid,name) values(76,'电池信息');
insert into tb_spec_group (cid,name) values(76,'屏幕');
insert into tb_spec_group (cid,name) values(76,'主体');
insert into tb_spec_group (cid,name) values(76,'规格尺寸');

-- 规格参数表
DROP TABLE IF EXISTS `tb_spec_param`;
CREATE TABLE `tb_spec_param`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `cid` bigint(20) NOT NULL COMMENT '商品分类id',
    `group_id` bigint(20) NOT NULL,
    `name` varchar(255) COMMENT '参数名',
    `numeric` tinyint(1) NOT NULL COMMENT '是否是数字型参数，true或者false',
    `unit` varchar(255) DEFAULT '' COMMENT '数字类型参数的单位，其他类型可以不填',

    `generic` tinyint(1) NOT NULL COMMENT '是否是SKU通用属性，true或者false',
    `searching` tinyint(1) NOT NULL COMMENT '是否用于过滤搜索,true或者false',
    `segments` varchar(1000) DEFAULT '' COMMENT '数值类型参数，如果需要搜索，则添加分段间隔值，如CPU频率间隔：0.5-1.0',
    PRIMARY KEY(`id`),
    KEY `key_group` (`group_id`),
    KEY `key_category` (`cid`)
)ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COMMENT '规格参数组下的参数名';
insert into tb_spec_param (group_id,name,`numeric`,unit,generic,searching) values (14,"机身颜色",0,'',1,0);
insert into tb_spec_param (group_id,name,`numeric`,unit,generic,searching) values (14,"机身重量(g)",1,'g',1,0);
insert into tb_spec_param (group_id,name,`numeric`,unit,generic,searching) values (14,"机身材质",0,'',1,0);

-- 表的垂直拆分——将SPU表拆分为tb_spu表和tb_spu_detail表
-- ----------------------------
-- Table structure for tb_spu
-- ----------------------------
DROP TABLE IF EXISTS `tb_spu`;
CREATE TABLE `tb_spu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'spu id',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题，就是商品名称',
  `sub_title` varchar(255) DEFAULT '' COMMENT '子标题',
  `cid1` bigint(20) NOT NULL COMMENT '1级类目id',
  `cid2` bigint(20) NOT NULL COMMENT '2级类目id',
  `cid3` bigint(20) NOT NULL COMMENT '3级类目id',
  `brand_id` bigint(20) NOT NULL COMMENT '商品所属品牌id',
  `saleable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否上架，0下架，1上架',
  `valid` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效，0已删除，1有效',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8 COMMENT='spu表，该表描述的是一个抽象性的商品，比如 iphone8';
-- ----------------------------
-- Table structure for tb_spu_detail
-- ----------------------------
DROP TABLE IF EXISTS `tb_spu_detail`;
CREATE TABLE `tb_spu_detail` (
  `spu_id` bigint(20) NOT NULL,
  `description` text COMMENT '商品描述信息',
  `specifications` varchar(3000) NOT NULL DEFAULT '' COMMENT '全部规格参数数据',
  `spec_template` varchar(1000) NOT NULL COMMENT '特有规格参数及可选值信息，json格式',
  `packing_list` varchar(1000) DEFAULT '' COMMENT '包装清单',
  `after_service` varchar(1000) DEFAULT '' COMMENT '售后服务',
  PRIMARY KEY (`spu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






























































































