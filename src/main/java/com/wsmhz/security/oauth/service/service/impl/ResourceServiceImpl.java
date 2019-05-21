package com.wsmhz.security.oauth.service.service.impl;

import com.wsmhz.common.business.service.impl.BaseServiceImpl;
import com.wsmhz.security.oauth.service.domain.entity.Resource;
import com.wsmhz.security.oauth.service.domain.entity.ResourceType;
import com.wsmhz.security.oauth.service.mapper.ResourceMapper;
import com.wsmhz.security.oauth.service.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * create by tangbj on 2018/4/27
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public Resource selectByPrimaryKeyAndType(Long id, ResourceType resourceType) {
        Example example = new Example(Resource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        if(resourceType != null){
            criteria.andEqualTo("type",resourceType);
        }
        return resourceMapper.selectOneByExample(example);
    }
}
