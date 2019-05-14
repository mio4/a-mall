/**

**/

CREATE TABLE `tb_spec_group`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键'，
    `cid` bigint(20) NOT NULL COMMENT '商品分类id，一个分类下有多个规格组',
    `name` varchar(50) NOT NULL COMMENT '规格组的名称',
    PRIMARY KEY (`id`),
    KEY `key_category` (`cid`)
)ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT '规格参数的分组表，每个商品下有多个规格参数组';

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

































































































