package com.wsmhz.security.oauth.service.authentication;

import com.google.common.collect.Lists;
import com.wsmhz.security.oauth.service.domain.entity.Admin;
import com.wsmhz.security.oauth.service.domain.entity.Resource;
import com.wsmhz.security.oauth.service.domain.entity.User;
import com.wsmhz.security.oauth.service.service.AdminService;
import com.wsmhz.security.oauth.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * create by tangbj on 2018/5/6
 */
@Slf4j
@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService,SocialUserDetailsService{

    private static String USER = "user";
    private static String ADMIN = "admin";

//    @Autowired
//    private UserApi userApi;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username){
        String prefix = StringUtils.substringBefore(username, "_");
        String name = StringUtils.substringAfter(username, "_");
        log.info("登陆前缀名为:" + prefix);
        if(USER.equals(prefix)){
            User user = userService.selectByUsername(name);
            log.info("登陆用户为:" + name);
            return user;
        }else if(ADMIN.equals(prefix)){
            Admin admin = adminService.selectByUsername(name);
            Set<Resource> resources = adminService.selectAllResourceByAdmin(new Admin(admin.getId()), null);
            admin.setResources(Lists.newArrayList(resources));
            log.info("登陆用户为:" + name);
            return admin;
        }
        return null;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        return null;
    }
}
