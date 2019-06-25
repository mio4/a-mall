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
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.UnmappedTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
/**
 * 对应前端search.html,构造搜索条件
 */
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

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
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

        String key = request.getKey();
        if(StringUtils.isBlank(key)){
            return null;
        }

        //创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //只查询指定字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
        //分页操作
        queryBuilder.withPageable(PageRequest.of(page,size));
        //过滤
        //基本查询条件
        QueryBuilder basicQuery = QueryBuilders.matchQuery("all",key);
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",request.getKey()));

        //聚合品牌和分类
        //1.聚合分类
        String categoryAggName = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        //2.品牌聚合
        String brandAggName = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        //查询
//        Page<Goods> result = goodsRepository.search(queryBuilder.build());
        AggregatedPage<Goods> result = elasticsearchTemplate.queryForPage(queryBuilder.build(),Goods.class);

        //解析结果
        //1. 分页结果
        long total = result.getTotalElements();
        int totalPage = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        //2. 聚合结果
        Aggregations aggs = result.getAggregations();
        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));

        //规格参数聚合
//        List<Map<String,Object>> specs = null;
//        if(categories != null && categories.size() == 1){
//            //商品分类存在并且数量为1
//            specs = buildSpecificationAgg(categories.get(0).getId(),basicQuery);
//        }

        //封装返回
//        return new PageResult<>(total,totalPage,goodsList);
//        return new SearchResult(total,totalPage,goodsList,categories,brands,specs);
        //TODO FIXME 修复bug
        return new SearchResult(total,totalPage,goodsList,categories,brands,null);
    }

    /**
     * 解析聚合
     * @param cid
     * @param basicQuery
     * @return
     */
    private List<Map<String,Object>> buildSpecificationAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String,Object>> specs = new ArrayList<>();
        //查询需要聚合的规格参数
        List<SpecParam> params = specificationClient.queryParamList(null,cid,true);
        //聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //附加查询条件
        queryBuilder.withQuery(basicQuery);
        //进行聚合
        for(SpecParam specParam : params){
            String name = specParam.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        }
        //获取结果
        AggregatedPage<Goods> result = elasticsearchTemplate.queryForPage(queryBuilder.build(),Goods.class);
        //解析结果
        Aggregations aggs = result.getAggregations();
        for(SpecParam specParam : params){
            String name = specParam.getName();
            StringTerms terms = aggs.get(name);
            List<String> options = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsString())
                    .collect(Collectors.toList());

//            UnmappedTerms terms = aggs.get(name);


            //准备map
            Map<String,Object> map = new HashMap<>();
            map.put("key",name);
            map.put("options",options);

            specs.add(map);
        }
        return specs;
    }

    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
            //TODO JDK8
            List<Long> ids = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Category> categories = categoryClient.queryCategoryByIds(ids);
            return categories;
        } catch (Exception e){
            log.error("[搜索服务]-查询分类异常",e);
            return null;
        }
    }

    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            //TODO JDK8
            List<Long> ids = terms.getBuckets()
                    .stream().map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandByIds(ids);
            return brands;
        } catch (Exception e){
            log.error("[搜索服务]-查询品牌异常",e);
            return null;
        }
    }

    public void createOrUpdateIndex(Long spuId) throws LyException {
        //查询SPU
        Spu spu = goodsClient.querySpuById(spuId);
        //构建商品详情
        Goods goods = buildGoods(spu);
        //存入索引库
        goodsRepository.save(goods);
    }

    public void deleteIndex(Long spuId) {
        //删除索引库对应商品
        goodsRepository.deleteById(spuId);
    }
}
