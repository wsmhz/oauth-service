package com.wsmhz.security.oauth.service.service;

import com.wsmhz.common.business.service.BaseService;
import com.wsmhz.security.oauth.service.domain.entity.Resource;
import com.wsmhz.security.oauth.service.domain.entity.ResourceType;

/**
 * create by tangbj on 2018/4/27
 */
public interface ResourceService extends BaseService<Resource> {
    /**
     * 根据主键和资源类型查询资源
     * @param id
     * @param resourceType
     * @return
     */
    Resource selectByPrimaryKeyAndType(Long id, ResourceType resourceType);
}
