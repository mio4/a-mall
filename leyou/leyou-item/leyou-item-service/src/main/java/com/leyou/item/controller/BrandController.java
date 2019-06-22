package com.leyou.item.controller;

import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌
     * @param page 当前页码
     * @param rows 一页有多少列
     * @param sortBy 排序依据的字段
     * @param desc 是否降序
     * @param key 查询关键词
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
            @RequestParam(value = "key",required = false) String key
    ) throws LyException {
        PageResult<Brand> result = brandService.queryBrandByPage(page,rows,sortBy,desc,key);
        return ResponseEntity.ok(result);
    }


    /**
     * 新增品牌
     * TODO 修改axios postdata编码的问题
     * 1. 使用Map获取很容易出现问题
     * 2. 尽量直接使用RequestParam获取参数
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(
            @RequestBody Map map
            ) throws LyException {
        Brand brand = new Brand();
        brand.setName((String)map.get("name"));
        brand.setImage((String) map.get("image"));
        String letter = (String) map.get("letter");
        Character c = letter.charAt(0);
        brand.setLetter(c);

        Object raw_categories = map.get("categories");
        List<Integer> categories = (List<Integer>)raw_categories;
        brandService.saveBrand(brand,categories);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 通过id查询品牌
     * @param cid
     * @return
     */
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid) throws LyException {
        return ResponseEntity.ok(brandService.queryBrandByCid(cid));
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     * @throws LyException
     */
    @GetMapping("{cid}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("cid") Long id) throws LyException {
        return ResponseEntity.ok(brandService.queryById(id));
    }

    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List<Long> ids) throws LyException {
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }





}
