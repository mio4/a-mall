package com.es.repository;

import com.es.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
    List<Item> findByPriceBetween(Double begin, Double end);
}
