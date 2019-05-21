package com.wsmhz.security.oauth.service.service;

import com.wsmhz.common.business.service.BaseService;
import com.wsmhz.security.oauth.service.domain.entity.Resource;
import com.wsmhz.security.oauth.service.domain.entity.ResourceType;
import com.wsmhz.security.oauth.service.domain.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * create by tangbj on 2018/4/27
 */
public interface RoleService extends BaseService<Role> {
    /**
     * 根据角色查询所有资源集合
     * @param roleList
     * @param resourceType
     * @return
     */
    Set<Resource> selectAllResourceByRole(List<Role> roleList, ResourceType resourceType);
    /**
     * 递归查询角色用户的资源（带树形结构）
     * @param roleList
     * @param resourceType
     * @return
     */
    List<Resource> selectResourceWithChildByRole(List<Role> roleList, ResourceType resourceType);
}
