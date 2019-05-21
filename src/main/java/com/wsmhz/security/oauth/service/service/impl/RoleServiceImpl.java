package com.wsmhz.security.oauth.service.service.impl;

import com.wsmhz.common.business.service.impl.BaseServiceImpl;
import com.wsmhz.security.oauth.service.domain.entity.Resource;
import com.wsmhz.security.oauth.service.domain.entity.ResourceType;
import com.wsmhz.security.oauth.service.domain.entity.Role;
import com.wsmhz.security.oauth.service.service.ResourceService;
import com.wsmhz.security.oauth.service.service.RoleResourceService;
import com.wsmhz.security.oauth.service.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * create by tangbj on 2018/4/27
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleResourceService roleResourceService;

    @Override
    public Set<Resource> selectAllResourceByRole(List<Role> roleList, ResourceType resourceType) {
        Set<Resource> targetResourceSet = new HashSet<>();
        for (Role role : roleList) {
            List<Long> resourceIds = roleResourceService.getAllResourceIdsByRoleId(role.getId());
            for (Long resourceId : resourceIds) {//该角色拥有的所有资源Id集合
                Resource resource = resourceService.selectByPrimaryKeyAndType(resourceId,resourceType);
                if(resource != null && !targetResourceSet.contains(resource)){
                    targetResourceSet.add(resource);
                }
            }
        }
        return targetResourceSet;
    }

    // 所有资源set<Resource>
    // List<role>
    //      1
    //      结果集：取出所有List<resource>  父亲
    //             根据parentId找出所有的List<Resource>  1
    //             遍历是否存在  2
    //             不存在 child添加  3
    //               递归 1
    @Override
    public List<Resource> selectResourceWithChildByRole(List<Role> roleList, ResourceType resourceType) {
        Map<Integer,Resource> allResourceMap= new HashMap<>();
        List<Resource> targetResourceList = new ArrayList<>();
        for (Role role : roleList) {
            List<Long> resourceIds = roleResourceService.getAllResourceIdsByRoleId(role.getId());
            for (Long resourceId : resourceIds) {//该角色拥有的所有资源Id集合
                Resource resource = resourceService.selectByPrimaryKeyAndType(resourceId,resourceType);
                if(resource != null && StringUtils.equals(resource.getParentId().toString(),"0") ){
                    if(allResourceMap.get(resource.getId().intValue()) == null) {  //所有父亲
                        targetResourceList.add(resource);
                        allResourceMap.put(resource.getId().intValue(),resource);
                    }
                    setChildResourceByParent(resourceIds,allResourceMap,allResourceMap.get(resource.getId().intValue()),resourceType);
                }
            }
        }
        return targetResourceList;
    }

    private void setChildResourceByParent(List<Long> resourceIds,Map<Integer,Resource> allResourceMap,Resource resource,ResourceType resourceType){
        Example example = new Example(Resource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",resource.getId());
        if(resourceType != null){
            criteria.andEqualTo("type",resourceType);
        }
        List<Resource> childResourceList = resourceService.selectByExample(example); //获取该资源的一级子资源
        for (Resource childResource : childResourceList) {
            if(resourceIds.contains(childResource.getId())) { //判断该角色是否拥有该资源
                if( allResourceMap.get(childResource.getId().intValue()) == null){  //排重
                    List<Resource> oldChild = resource.getChildren();
                    oldChild.add(childResource); //取出之前的子资源，并加入新的子资源
                    resource.setChildren(oldChild);
                    allResourceMap.put(resource.getId().intValue(),resource);
                    allResourceMap.put(childResource.getId().intValue(),childResource);
                }
                setChildResourceByParent(resourceIds,allResourceMap,childResource,resourceType);
            }
        }
    }


}
