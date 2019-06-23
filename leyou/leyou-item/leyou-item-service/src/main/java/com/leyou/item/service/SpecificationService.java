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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<SpecParam> queryParamList(Long gid,Long cid,Boolean searching) throws LyException {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
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

    public List<SpecGroup> queryListByCid(Long cid) throws LyException {
        //查询规格组
        List<SpecGroup> specGroups = queryGroupByCid(cid);
        //查询组内参数
        List<SpecParam> params = queryParamList(null, cid, null);
        //填充params到specGroups
        Map<Long, List<SpecParam>> param_map = new HashMap<>();
        for(SpecParam specParam : params){
            if(!param_map.containsKey(specParam.getGroupId())){ //组id在map中不存在
                param_map.put(specParam.getGroupId(),new ArrayList<>());
            }
            param_map.get(specParam.getGroupId()).add(specParam);
        }

        for(SpecGroup specGroup : specGroups){
            specGroup.setParams(param_map.get(specGroup.getId()));
        }

        return specGroups;
    }
}
