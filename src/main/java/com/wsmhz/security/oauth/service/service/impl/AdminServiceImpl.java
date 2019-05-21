package com.wsmhz.security.oauth.service.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wsmhz.security.oauth.service.domain.entity.*;
import com.wsmhz.security.oauth.service.mapper.AdminMapper;
import com.wsmhz.security.oauth.service.mapper.RoleMapper;
import com.wsmhz.security.oauth.service.service.RoleAdminService;
import com.wsmhz.security.oauth.service.service.RoleService;
import com.wsmhz.common.business.service.impl.BaseServiceImpl;
import com.wsmhz.security.oauth.service.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * create by tangbj on 2018/4/27
 */
@Service
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleAdminService roleAdminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Resource> selectMenuTreeByAdmin(Admin admin, ResourceType resourceType) {
        List<Role> roleList = selectAllRoleByAdmin(admin);
        return roleService.selectResourceWithChildByRole(roleList,resourceType);
    }

    @Override
    public Set<Resource> selectAllResourceByAdmin(Admin admin, ResourceType resourceType) {
        List<Role> roleList = selectAllRoleByAdmin(admin);
        return roleService.selectAllResourceByRole(roleList,resourceType);
    }

    @Override
    public List<Role> selectAllRoleByAdmin(Admin admin) {
        List<Role> list = new ArrayList<>();
        for (RoleAdmin role_admin : roleAdminService.selectAllByAdmin(admin)) {
            Role role = roleMapper.selectByPrimaryKey(role_admin.getRoleId());
            if( role != null){
                list.add(role);
            }
        }
        return list;
    }

    @Override
    public Admin selectByUsername(String username) {
        Example example = new Example(Admin.class);
        example.createCriteria().andEqualTo("username", username);
        return  adminMapper.selectOneByExample(example);
    }

    @Override
    public PageInfo<Admin> selectPageListWithSearch(Integer pageNum, Integer pageSize, String username, boolean status) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(Admin.class);
        example.setOrderByClause("update_date desc");
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(username)){
            criteria.orLike("username","%"+username+"%");
        }
        criteria.andEqualTo("status",status);
        List<Admin> list = adminMapper.selectByExample(example);
        return new PageInfo<>(list);
    }


}
