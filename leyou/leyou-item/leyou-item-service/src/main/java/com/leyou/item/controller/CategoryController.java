package com.leyou.item.controller;

import com.leyou.common.exception.LyException;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点pid查询商品分类
     * @param pid
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid") Long pid) throws LyException {
        return ResponseEntity.ok().body(categoryService.queryCategoryListByPid(pid));
    }

    /**
     * 根据商品分类id查询名称
     * @param ids
     * @return
     * @throws LyException
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids) throws LyException {
        return ResponseEntity.ok(categoryService.queryByIds(ids));
    }
}
