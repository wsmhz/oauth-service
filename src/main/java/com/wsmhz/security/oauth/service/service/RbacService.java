package com.wsmhz.security.oauth.service.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * create by tangbj on 2018/6/30
 */
public interface RbacService {
    /**
     * 资源权限拦截判断
     * @param request
     * @param authentication
     * @return
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
