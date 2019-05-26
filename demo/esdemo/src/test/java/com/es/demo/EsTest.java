package com.es.demo;

import com.es.EsApplication;
import com.es.pojo.Item;
import com.es.repository.ItemRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsApplication.class)
public class EsTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testCreate(){
        elasticsearchTemplate.createIndex(Item.class);
        elasticsearchTemplate.putMapping(Item.class);
    }

    @Test
    public void testInsert(){
        List<Item> list = new ArrayList<>();
        list.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3699.00,"http://image.leyou.com/123.jpg"));
        list.add(new Item(3L, "华为META10", " 手机", "华为", 4499.00, "http://image.leyou.com/3.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }


    @Test
    public void testFind(){
        Iterable<Item> all = itemRepository.findAll();
        for(Item item : all){
            System.out.println(item);
        }
    }

    @Test
    public void testQuery(){
        List<Item> res = itemRepository.findByPriceBetween(2000d,4000d);
        for(Item item : res){
            System.out.println(item);
        }
    }

    @Test
    public void testQuery2(){
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        //添加查询条件
        searchQueryBuilder.withQuery(QueryBuilders.matchQuery("title","手机"));
        //结果过滤
        searchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","title"},null));
        //结果排序
        searchQueryBuilder.withSort(SortBuilders.fieldSort("price"));
        //查询
        Page<Item> result = itemRepository.search(searchQueryBuilder.build());
    }

    @Test
    public void testDelete(){
    }

}
