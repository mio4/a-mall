package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    public List<Category> queryCategoryListByPid(Long pid) throws LyException {
        Category c = new Category();
        c.setParentId(pid);
        List<Category> result = categoryMapper.select(c);
        if(CollectionUtils.isEmpty(result)){ //404 not found
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return result;
    }

    /**
     * 查询多个id对应的category列表
     * @param ids
     * @return 查询结果
     */
    public List<Category> queryByIds(List<Long> ids) throws LyException {
        List<Category> result = categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(result)){ //404 not found
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return result;
    }


}
