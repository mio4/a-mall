package com.leyou.page.web;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * 页面需要的数据：
     * （1）SPU
     * （2）SPU对应所有SKU
     * （3）品牌
     * （4）商品分类
     * （5）商品规格参数、规格参数组
     * @param model
     * @param id
     * @return
     */
    @GetMapping("item/{id}.html")
    public String toItemPage(Model model,
                             @PathVariable("id") Long id){
        //查询模型数据
        Map<String,Object> attributes = pageService.loadModel(id);

        //添加模型数据
        model.addAllAttributes(attributes);

        //返回视图
        return "item";
    }
}
