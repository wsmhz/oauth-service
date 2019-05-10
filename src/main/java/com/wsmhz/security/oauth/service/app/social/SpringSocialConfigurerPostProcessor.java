package com.wsmhz.security.oauth.service.app.social;

import com.wsmhz.security.core.properties.SecurityConstants;
import com.wsmhz.security.core.social.WsmhzSpringSocialConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * create by tangbj on 2018/4/8
 * social bean处理器，替换wsmhzSocialSecurityConfig中的注册页面逻辑，app返回json
 */
@Component
public class SpringSocialConfigurerPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(StringUtils.equals(beanName, "wsmhzSocialSecurityConfig")){
			WsmhzSpringSocialConfigurer config = (WsmhzSpringSocialConfigurer)bean;
			config.signupUrl(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL);
			return config;
		}
		return bean;
	}

}
