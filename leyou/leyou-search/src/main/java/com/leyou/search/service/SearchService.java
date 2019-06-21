package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * TODO 将下面JDK8的特性多加理解
     * @param spu
     * @return
     * @throws LyException
     */
    public Goods buildGoods(Spu spu) throws LyException {
        //all-分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
        if(CollectionUtils.isEmpty(categories)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> category_names = new ArrayList<>();
        for(Category category : categories){
            category_names.add(category.getName());
        }
        //all-品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if(brand == null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        String all = spu.getTitle() + StringUtils.join(category_names,"") + brand.getName();

        //查询SKU
        List<Sku> skus = goodsClient.querySkuBySpuId(spu.getId());
        if(skus == null){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //价格列表
        Set<Long> prices = new HashSet<>();
        //对SKU进行处理
        List<Map<String,Object>> sku_list = new ArrayList<>();
        for(Sku sku : skus){
            Map<String,Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            map.put("images",StringUtils.substringBefore(sku.getImages(),","));
            sku_list.add(map);

            prices.add(sku.getPrice());
        }

        //查询规格参数&商品详情
        List<SpecParam> params = specificationClient.queryParamList(null,spu.getCid3(),true);
        SpuDetail spuDetail = goodsClient.queryDetailById(spu.getId());
        //通用规格参数
        Map<Long,String> genericSpec = JsonUtils.toMap(spuDetail.getGenericSpec(),Long.class,String.class);
        //特有规格参数
        Map<Long,List<String>> specialSpec = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {});
        //处理规格参数
//        printList(params);
        Map<String,Object> specs = new HashMap<>();
        for(SpecParam param : params){
            String key = param.getName();
            Object value;
            if(param.getGeneric()){
                value = genericSpec.get(param.getId());
                //判断是否是数值类型
                if(param.getNumeric()){
                    value = chooseSegment(value.toString(),param);
                }
            }
            else{
                value = specialSpec.get(param.getId());
            }
            //存入map
            specs.put(key,value);
        }

        Goods goods = new Goods();

        goods.setId(spu.getId());
        goods.setAll(all); //搜索字段，包含标题，分类，品牌，信息
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setPrice(prices); //所有sku对象的价格集合
        goods.setSkus(JsonUtils.toString(sku_list)); //所有sku对象的JSON格式集合
        goods.setSpecs(null); //TODO 所有可搜索的规格参数

        return goods;
    }

    private String chooseSegment(String value,SpecParam param){
        double val = NumberUtils.toDouble(value);
        String result = "其他";
        for(String segment : param.getSegments().split(",")){
            String[] segs = segment.split("-");
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[0]);
            }
            //判断是否在数值范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + param.getUnit() + "以上";
                }
                else if(begin == 0){
                    result = segs[1] + param.getUnit() + "以下";
                }
                else{
                    result = segment + param.getUnit();
                }
            }
        }
        return result;
    }

    private void printList(List<SpecParam> list){
        for(SpecParam o : list){
            System.out.println(o);
        }
    }

    public PageResult<Goods> search(SearchRequest request) {
        int page = request.getPage()-1;
        int size = request.getSize();

        //创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //只查询指定字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        //分页操作
        queryBuilder.withPageable(PageRequest.of(page,size));
        //过滤
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",request.getKey()));
        //查询
        Page<Goods> result = goodsRepository.search(queryBuilder.build());
        //解析结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Goods> goodsList = result.getContent();

        //封装返回
        return new PageResult<>(total,totalPage,goodsList);
    }
}
