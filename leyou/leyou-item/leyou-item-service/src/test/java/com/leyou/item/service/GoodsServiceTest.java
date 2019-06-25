package com.leyou.item.service;

import com.leyou.common.exception.LyException;
import com.leyou.item.pojo.Spu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    @Test
    public void updateGoods() throws LyException {
        Spu spu = new Spu();
        goodsService.updateGoods(spu);
    }

    @Test
    public void deleteGoods() throws LyException {
        goodsService.deleteGoods(101L);
    }

}