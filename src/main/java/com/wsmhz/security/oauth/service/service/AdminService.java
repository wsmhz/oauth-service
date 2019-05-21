package com.wsmhz.security.oauth.service.service;

import com.github.pagehelper.PageInfo;
import com.wsmhz.common.business.service.BaseService;
import com.wsmhz.security.oauth.service.domain.entity.Admin;
import com.wsmhz.security.oauth.service.domain.entity.Resource;
import com.wsmhz.security.oauth.service.domain.entity.ResourceType;
import com.wsmhz.security.oauth.service.domain.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * create by tangbj on 2018/4/27
 */
public interface AdminService extends BaseService<Admin> {
    /**
     * 根据管理员和资源类型生成资源菜单树
     * @param admin
     * @param resourceType 可为空
     * @return
     */
    List<Resource> selectMenuTreeByAdmin(Admin admin, ResourceType resourceType);

    /**
     * 根据管理员和资源类型查询出所有资源集合
     * @param admin
     * @param resourceType 可为空
     * @return
     */
    Set<Resource> selectAllResourceByAdmin(Admin admin, ResourceType resourceType);

    /**
     * 查询管理员所有角色
     * @param admin
     * @return
     */
    List<Role> selectAllRoleByAdmin(Admin admin);

    /**
     * 根据用户名查询管理员
     * @param username
     * @return
     */
    Admin selectByUsername(String username);

    /**
     * 管理员列表（带搜索）
     * @param pageNum
     * @param pageSize
     * @param username 用户名 可为空
     * @param status 状态 可为空
     * @return
     */
    PageInfo<Admin> selectPageListWithSearch(Integer pageNum, Integer pageSize, String username, boolean status);

}
