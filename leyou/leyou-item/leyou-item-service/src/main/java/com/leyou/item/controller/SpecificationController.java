package com.leyou.item.controller;

import com.leyou.common.exception.LyException;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询规格组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) throws LyException {
        return ResponseEntity.ok(specificationService.queryGroupByCid(cid));
    }

    @PostMapping("group")
    public void saveGroup(@RequestBody SpecGroup group){
        specificationService.saveGroup(group);
    }

    /**
     * 根据分组id查询规格对应参数列表
     * @param gid
     * @return
     * @throws LyException
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamByGid(@RequestParam("gid") Long gid) throws LyException {
        return ResponseEntity.ok(specificationService.queryParamByGid(gid));
    }

    /**
     * 添加新的规格参数
     * 使用@RequestBody将Post Body的数据封装到Bean对象
     * @param specParam
     */
    @PostMapping("param")
    public void addParam(@RequestBody SpecParam specParam){
        specificationService.addParam(specParam);
    }

}
