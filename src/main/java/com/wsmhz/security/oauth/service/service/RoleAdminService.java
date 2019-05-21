package com.wsmhz.security.oauth.service.service;

import com.wsmhz.common.business.service.BaseService;
import com.wsmhz.security.oauth.service.domain.entity.Admin;
import com.wsmhz.security.oauth.service.domain.entity.RoleAdmin;

import java.util.List;

/**
 * create by tangbj on 2018/4/27
 */
public interface RoleAdminService extends BaseService<RoleAdmin> {
    List<RoleAdmin> selectAllByAdmin(Admin admin);

    Integer insertBatch(Long adminId, String roleIds);

}