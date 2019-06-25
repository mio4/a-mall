package com.leyou.page.service;

import com.leyou.item.pojo.*;
import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

    @Autowired
    private TemplateEngine templateEngine;

    //TODO 文件保存路径，服务器上和本机处于不同环境，如何自由切换
    private String STATIC_FILE_PATH = "";

    public Map<String,Object> loadModel(Long id) {
        Map<String,Object> result = new HashMap<>();
        //查询SPU
        Spu spu = goodsClient.querySpuById(id);
        //得到SKUS
        List<Sku> skus = spu.getSkus();
        //得到详情
        SpuDetail spuDetail = spu.getSpuDetail();
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询商品分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数
        List<SpecGroup> specs = specificationClient.queryListByCid(spu.getCid3());
//        String spuDetail = null;
        //特殊规格参数id-name键值对
        Map<Long,String> paramMap = getParamMap(specs);

//        //FIXME
//        System.out.println(spu);
//        printList(skus);
//        System.out.println(spuDetail);
//        System.out.println(brand);
//        printList(categories);
//        printList(specs);

        //TODO 这里可以根据页面简化需要存放在Model中的key-value
        result.put("spu",spu);
        result.put("skus",skus);
        result.put("spuDetail",spuDetail);
        result.put("brand",brand);
        result.put("categories",categories);
        result.put("groups",specs);
        result.put("paramMap",paramMap);
        return result;
    }

//    private void printList(List list){
//        for(Object o : list){
//            System.out.println(o);
//        }
//    }

    /**
     * 封装好特有规格的HashMap
     * @param specs
     * @return
     */
    private Map<Long,String> getParamMap(List<SpecGroup> specs){
        Map<Long,String> result = new HashMap<>();
        for(SpecGroup specGroup : specs){
            for(SpecParam specParam : specGroup.getParams()){
                if(!specParam.getGeneric()){
                    result.put(specParam.getId(),specParam.getName());
                }
            }
        }
        return result;
    }

    /**
     * 生成静态页面，保存到本地|服务器
     * @param spuId
     */
    public void createHtml(Long spuId){
        //上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        //输出流
        File dest = new File("E:\\GitHub\\a-mall\\local\\static page", spuId + ".html");
        try {
            if(dest.exists()){
                dest.delete();
            }
            PrintWriter writer = new PrintWriter(dest);
            //生成HTML
            templateEngine.process("item",context,writer);
        } catch (FileNotFoundException e) {
            log.error("[静态页面服务] 生成静态页面异常");
            e.printStackTrace();
        }
    }

    /**
     * 删除（服务器|本地）静态页面
     * @param spuId
     */
    public void deleteHtml(Long spuId){
        File dest = new File("E:\\GitHub\\a-mall\\local\\static page", spuId + ".html");
        if(dest.exists()){
            dest.delete();
        }
    }

}
