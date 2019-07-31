package com.wsmhz.security.oauth.service.authentication;

import com.google.common.collect.Lists;
import com.wsmhz.common.business.utils.RedisUtil;
import com.wsmhz.security.rbac.service.api.api.AdminApi;
import com.wsmhz.security.rbac.service.api.constant.RbacConstant;
import com.wsmhz.security.rbac.service.api.domain.vo.AdminLoginInfoVo;
import com.wsmhz.security.resource.service.api.api.UserApi;
import com.wsmhz.security.resource.service.api.domain.form.UsernameForm;
import com.wsmhz.security.resource.service.api.domain.vo.LoginUserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;


/**
 * create by tangbj on 2018/5/6
 */
@Slf4j
@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService,SocialUserDetailsService{

    private static String USER = "user";
    private static String ADMIN = "admin";

    @Autowired
    private UserApi userApi;
    @Autowired
    private AdminApi adminApi;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String username){
        String prefix = StringUtils.substringBefore(username, "_");
        String name = StringUtils.substringAfter(username, "_");
        log.info("登陆前缀名为:" + prefix);
        if(USER.equals(prefix)){
            LoginUserVo loginUserVo = userApi.selectByUsername(UsernameForm.builder()
                    .username(name).build());
            log.info("登陆用户为:" + loginUserVo.getUsername());
            return new User(loginUserVo.getUsername(),loginUserVo.getPassword() ,true, true, true, true,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
        } else if(ADMIN.equals(prefix)){
            AdminLoginInfoVo adminLoginInfoVo = adminApi.selectByUsernameForLogin(name);
            // 权限资源放入缓存中
            for (String resource : adminLoginInfoVo.getResources()) {
                redisUtil.getSet().add(RbacConstant.RESOURCE_TOKEN_KEY + adminLoginInfoVo.getUsername(), resource);
            }
            log.info("登陆用户为:" + adminLoginInfoVo.getUsername());
            return new User(adminLoginInfoVo.getUsername(),adminLoginInfoVo.getPassword(),true, true, true, true,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
        }
        return null;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        return null;
    }
}
