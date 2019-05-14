package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_spu_detail")
public class SpuDetail {
    @Id
    private Long spuId; // SPU对应ID

    private String description; //商品描述
    private String specialSpec; //商品特殊规格名称
    private String genericSpec; //商品的通用规格名称
    private String packingList; //包装清单
    private String afterService; //售后服务

}
