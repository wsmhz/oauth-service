package com.wsmhz.security.oauth.service.authentication;

import com.wsmhz.security.core.authorize.AuthorizeConfigProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * create by tangbj on 2018/5/23
 */
//@Component
//@Order(Integer.MAX_VALUE)
public class WebAuthorizeConfigProvider {

//    @Override
//    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
//        config
//                .antMatchers("/api/product/**","/*/api/category/**","/api/user/register","/*/api/**/aliPayCallback")
//                    .permitAll()
//                .antMatchers("/*/manage/**")
//                    .access("@rbacService.hasPermission(request, authentication)")
//                .anyRequest().authenticated();
//        return true;
//    }
}
