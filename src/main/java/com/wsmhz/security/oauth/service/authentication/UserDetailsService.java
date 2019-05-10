package com.wsmhz.security.oauth.service.authentication;

import com.wsmhz.security.oauth.service.domain.entity.User;
import com.wsmhz.security.oauth.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;


/**
 * create by tangbj on 2018/5/6
 */
@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService,SocialUserDetailsService{

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userService.selectByUsername(username);
        logger.info("登陆用户名:" + username);
        return user;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        return null;
    }
}
