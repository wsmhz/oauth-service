package com.wsmhz.security.oauth.service.service.impl;

import com.wsmhz.common.business.service.impl.BaseServiceImpl;
import com.wsmhz.security.oauth.service.domain.entity.RoleResource;
import com.wsmhz.security.oauth.service.mapper.RoleResourceMapper;
import com.wsmhz.security.oauth.service.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * create by tangbj on 2018/4/27
 */
@Service
public class RoleResourceServiceImpl extends BaseServiceImpl<RoleResource> implements RoleResourceService {

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Override
    public Integer insertBatch(Long roleId, String resourceIds) {
        int results = 0;
        String[] ids = resourceIds.split(",");
        this.deleteByExample(new RoleResource(),"roleId",roleId);
        for (String id : ids) {
            RoleResource roleResource = new RoleResource();
            roleResource.setResourceId(Long.valueOf(id));
            roleResource.setRoleId(roleId);
            int result = roleResourceMapper.insertSelective(roleResource);
            results = results + result;
        }
        return results;
    }

    @Override
    public List<Long> getAllResourceIdsByRoleId(Long roleId) {
        RoleResource roleResource = new RoleResource(roleId);
        List<RoleResource> roleResourceList = roleResourceMapper.select(roleResource);
        List<Long> resourceIds = new ArrayList<>();
        for (RoleResource role_resource : roleResourceList) {
            resourceIds.add(role_resource.getResourceId()); //该角色拥有的所有资源Id集合
        }
        return resourceIds;
    }
}
