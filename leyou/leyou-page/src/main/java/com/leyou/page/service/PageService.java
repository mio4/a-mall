package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String,Object> loadModel(Long id) {
        Map<String,Object> result = new HashMap<>();
        //查询SPU
        Spu spu = goodsClient.querySpuById(id);
        //得到SKUS
        List<Sku> skus = spu.getSkus();
        //得到详情
        SpuDetail detail = spu.getSpuDetail();
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数
        List<SpecGroup> specs = specificationClient.queryListByCid(spu.getCid3());

        //TODO 这里可以根据页面简化需要存放在Model中的key-value
        result.put("spu",spu);
        result.put("skus",skus);
        result.put("detail",detail);
        result.put("brand",brand);
        result.put("categories",categories);
        result.put("specs",specs);
        return result;
    }
}
