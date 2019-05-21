package com.wsmhz.security.oauth.service.service.impl;

import com.wsmhz.security.oauth.service.domain.entity.Admin;
import com.wsmhz.security.oauth.service.domain.entity.Resource;
import com.wsmhz.security.oauth.service.service.RbacService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * create by tangbj on 2018/6/30
 */
@Service("rbacService")
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;
        if (principal instanceof Admin) {
            //如果用户名是admin，就永远返回true
            if (StringUtils.equals(((Admin) principal).getUsername(), "admin")) {
                return true;
            } else {
                // 读取用户所拥有权限的所有URL
                List<Resource> resources = ((Admin) principal).getResources();
                // 所有管理员都可以获取到自己的资源
                Resource exResource = new Resource();
                exResource.setUrl("manage/admin/*/resource");
                resources.add(exResource);
                String uri = StringUtils.substringAfter(request.getRequestURI(),"/");
                for (Resource resource : resources) {
                    if(StringUtils.isNotBlank(resource.getUrl()) && antPathMatcher.match(resource.getUrl(), uri)){
                        hasPermission = true;
                        break;
                    }
                }
            }
        }
        return hasPermission;
    }


}
