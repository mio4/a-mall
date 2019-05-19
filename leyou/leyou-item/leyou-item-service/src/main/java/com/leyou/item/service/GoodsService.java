package com.leyou.item.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;



    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) throws LyException {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        //排序
        example.setOrderByClause("last_update_time DESC");
        //查询
        List<Spu> res = spuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(res)){
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        loadCategoryAndBrandName(res);
        //解析结果
        PageInfo<Spu> info = new PageInfo<>(res,rows);
        return new PageResult<>(info.getTotal(),info.getPageNum(),res);
    }

    /**
     *
     * @param res
     * @throws LyException
     */
    private void loadCategoryAndBrandName(List<Spu> res) throws LyException {
        for(Spu spu : res){
            //处理分类名称
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()))
            .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names,"/"));
            //处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        }
    }

    /**
     * 1. 新增SPU
     * 2. 新增SPU_Detail
     * 3. 新增SKU
     * 4. 新增Stock
     * @param spu
     */

    @Transactional
    public void saveGoods(Spu spu) throws LyException {
        Date currentDate = new Date(); //当前时间

        spu.setId(null);
        spu.setCreateTime(currentDate);
        spu.setLastUpdateTime(currentDate);
        spu.setValid(true);
        spu.setSaleable(true);

        int count = spuMapper.insert(spu);
        if(count != 1){
            throw new LyException(ExceptionEnum.GOODS_SAVE_FAILED);
        }

        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);

        List<Stock> stock_list = new ArrayList<>();
        List<Sku> skus = spu.getSkus();
        for(Sku sku : skus){
            sku.setCreateTime(currentDate);
            sku.setLastUpdateTime(currentDate);
            sku.setSpuId(spu.getId());
            int sku_count = skuMapper.insert(sku);
            if(sku_count != 1){
                throw new LyException(ExceptionEnum.GOODS_SAVE_FAILED);
            }

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stock_list.add(stock);
        }
        stockMapper.insertList(stock_list);

    }

    public SpuDetail queryDetailById(Long spuId) throws LyException {
        SpuDetail detail = spuDetailMapper.selectByPrimaryKey(spuId);
        if(detail == null){
            throw new LyException(ExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        return detail;
    }

    public List<Sku> querySkuBySpuId(Long spuId) throws LyException {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> sku_list = skuMapper.select(sku);
        if(sku_list == null){
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //查询库存
        for(Sku s : sku_list){
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if(stock == null){
                throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
            }
            s.setStock(stock.getStock());
        }

//        List<Long> ids = sku_list.stream().map(Sku::getId).collect(Collectors.toList());
//        List<Stock> stock_list = stockMapper.selectByIdList(ids);

        return sku_list;
    }
}
