package com.wsmhz.security.oauth.service.service.impl;

import com.wsmhz.common.business.service.impl.BaseServiceImpl;
import com.wsmhz.security.oauth.service.domain.entity.Admin;
import com.wsmhz.security.oauth.service.domain.entity.RoleAdmin;
import com.wsmhz.security.oauth.service.mapper.RoleAdminMapper;
import com.wsmhz.security.oauth.service.service.RoleAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by tangbj on 2018/4/27
 */
@Service
public class RoleAdminServiceImpl extends BaseServiceImpl<RoleAdmin> implements RoleAdminService {
    @Autowired
    private RoleAdminMapper roleAdminMapper;

    @Override
    public List<RoleAdmin> selectAllByAdmin(Admin admin) {
        RoleAdmin roleAdmin = new RoleAdmin();
        roleAdmin.setAdminId(admin.getId());
       return roleAdminMapper.select(roleAdmin);
    }

    @Override
    public Integer insertBatch(Long adminId, String roleIds) {
        int results = 0;
        String[] ids = roleIds.split(",");
        this.deleteByExample(new RoleAdmin(),"adminId",adminId);
        for (String id : ids) {
            RoleAdmin roleAdmin = new RoleAdmin();
            roleAdmin.setAdminId(adminId);
            roleAdmin.setRoleId(Long.valueOf(id));
            int result = roleAdminMapper.insertSelective(roleAdmin);
            results = results + result;
        }
        return results;
    }
}
