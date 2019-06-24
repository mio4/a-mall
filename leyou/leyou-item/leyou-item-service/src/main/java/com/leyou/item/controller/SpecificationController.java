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
     * 查询参数列表
     * @param gid group id
     * @param cid category id
     * @param searching 搜索字段
     * @return
     * @throws LyException
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamList(@RequestParam(value = "gid",required = false) Long gid,
                                                          @RequestParam(value = "cid",required = false) Long cid,
                                                          @RequestParam(value = "searching",required = false) Boolean searching) throws LyException {
        return ResponseEntity.ok(specificationService.queryParamList(gid,cid,searching));
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

    /**
     * 根据分类查询规格组和组内参数
     * @param cid
     * @return
     */
    @GetMapping("group")
    public ResponseEntity<List<SpecGroup>> queryListByCid(@RequestParam("cid") Long cid) throws LyException {
        return ResponseEntity.ok(specificationService.queryListByCid(cid));
    }

}
