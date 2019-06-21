package com.leyou.search.repository;

import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void testCreateIndex(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void loadData() throws LyException {
        int page = 1;
        int rows = 100;
        int size;
        do {
            //查询SPU信息
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> spuList = result.getItems();

            if(CollectionUtils.isEmpty(spuList)){
                break;
            }
printList(spuList);
            //构建成Goods
            List<Goods> goodsList = new ArrayList<>();
            for (Spu spu : spuList) {
                Goods goods = searchService.buildGoods(spu);
                goodsList.add(goods);
            }

            //存入索引库
            goodsRepository.saveAll(goodsList);

            page++;
            size = spuList.size();
        } while (size == 100);
    }

    private void printList(List<Spu> list){
        for(Spu spu : list){
            System.out.println(spu);
        }
    }

}