package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;
    @Autowired
    private SpecParamMapper paramMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) throws LyException {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> res = groupMapper.select(specGroup);
        if(CollectionUtils.isEmpty(res)){
            throw new LyException(ExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }
        return res;
    }

    public List<SpecParam> queryParamByGid(Long gid) throws LyException {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        List<SpecParam> res = paramMapper.select(specParam);
        if(CollectionUtils.isEmpty(res)){
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return res;
    }

    public void addParam(SpecParam specParam) {
        paramMapper.insert(specParam);
    }

    public void saveGroup(SpecGroup group) {
        groupMapper.insert(group);
    }
}
