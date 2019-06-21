package com.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "tb_spu")
public class Spu {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long brandId;
    private Long cid1; //一级类目
    private Long cid2; //二级类目
    private Long cid3; //三级类目
    private String title; //商品名称
    private String subTitle; //副标题
    private Boolean saleable; //是否上架
    private Boolean valid; //是否有效
    private Date createTime; //创建时间

    @JsonIgnore
    private Date lastUpdateTime; //最后修改时间

    //VO——可以新建一个类对应返回到页面的JavaBean
    @Transient
    private String cname; //category name
    @Transient
    private String bname; //brand name

    @Transient
    private List<Sku> skus;
    @Transient
    private SpuDetail spuDetail;


}
