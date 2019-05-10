package com.wsmhz.security.oauth.service.config;

import com.wsmhz.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.wsmhz.security.core.properties.SecurityConstants;
import com.wsmhz.security.core.properties.SecurityProperties;
import com.wsmhz.security.core.validate.code.ValidateCodeSecurityConfig;
import com.wsmhz.security.oauth.service.app.authentication.openid.OpenIdAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * create by tangbj on 2018/4/7
 * 资源服务器配置
 */
@Configuration
//@EnableResourceServer
public class WsmhzResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	protected AuthenticationSuccessHandler wsmhzAuthenticationSuccessHandler;

	@Autowired
	protected AuthenticationFailureHandler wsmhzAuthenticationFailureHandler;

	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Autowired
	private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;

	@Autowired
	private SpringSocialConfigurer wsmhzSocialSecurityConfig;

//	@Autowired
//	private AuthorizeConfigManager authorizeConfigManager;
//
//	@Autowired
//	private FormAuthenticationConfig formAuthenticationConfig;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
				.loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
				.successHandler(wsmhzAuthenticationSuccessHandler)
				.failureHandler(wsmhzAuthenticationFailureHandler);

			http.apply(validateCodeSecurityConfig)
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
							SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
							SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
							SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL,
							securityProperties.getBrowser().getLoginPage(),
							securityProperties.getBrowser().getLogoutPage(),
							securityProperties.getBrowser().getRegisterPage(),
							securityProperties.getBrowser().getSession().getSessionInvalidUrl()+".json",
							securityProperties.getBrowser().getSession().getSessionInvalidUrl()+".html",
							"/user/register"
					).permitAll()//设置不用验证的请求路径
					.anyRequest()
					.authenticated()
					.and()
				.csrf().disable();//禁用csrf跨越请求
	}
	
}