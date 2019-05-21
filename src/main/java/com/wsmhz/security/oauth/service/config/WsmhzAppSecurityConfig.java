package com.wsmhz.security.oauth.service.config;


import com.wsmhz.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.wsmhz.security.core.properties.SecurityConstants;
import com.wsmhz.security.core.properties.SecurityProperties;
import com.wsmhz.security.core.validate.code.ValidateCodeSecurityConfig;
import com.wsmhz.security.oauth.service.app.authentication.WsmhzLogoutSuccessHandler;
import com.wsmhz.security.oauth.service.app.authentication.openid.OpenIdAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * create by tangbj on 2019/5/10
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WsmhzAppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    protected AuthenticationSuccessHandler wsmhzAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler wsmhzAuthenticationFailureHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SpringSocialConfigurer wsmhzSocialSecurityConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(wsmhzAuthenticationSuccessHandler)
                .failureHandler(wsmhzAuthenticationFailureHandler)
                .and()

                .logout()
//                    .logoutUrl(SecurityConstants.DEFAULT_LOGOUT_PROCESSING_URL_FORM)
                .logoutSuccessHandler(logoutSuccessHandler)

                .and()
                    .apply(validateCodeSecurityConfig)
                .and()
                    .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                    .apply(wsmhzSocialSecurityConfig)
                .and()
                    .apply(openIdAuthenticationSecurityConfig)
                .and()
                .authorizeRequests()
                    .antMatchers(
                            SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                            SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM,
                            SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                            SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                            SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL,
                            "/register"
                    ).permitAll()
//                    .antMatchers("/oauth/**","/login/**","/logout/**").permitAll()
                    .anyRequest().authenticated();


//        http.csrf().disable();
//        http
//                .requestMatchers().antMatchers("/oauth/**","/login/**","/logout/**")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**").authenticated()
//                .and();
////                .formLogin().permitAll(); //新增login form支持用户登录及授权

    }


}
