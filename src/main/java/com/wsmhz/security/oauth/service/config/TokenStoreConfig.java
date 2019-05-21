package com.wsmhz.security.oauth.service.config;

import com.wsmhz.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * create by tangbj on 2018/4/8
 */
@Configuration
public class TokenStoreConfig {
	
	/**
	 * 使用redis存储token的配置，只有在wsmhz.security.oauth2.tokenStore配置为redis时生效
	 */
	@Configuration
	@ConditionalOnProperty(prefix = "wsmhz.security.oauth2", name = "tokenStore", havingValue = "redis")
	public static class RedisConfig {
		
		@Autowired
		private RedisConnectionFactory redisConnectionFactory;

		@Bean
		public TokenStore redisTokenStore() {

			return new RedisTokenStore(redisConnectionFactory);
		}
		
	}

	/**
	 * 使用jwt时的配置，默认生效
	 */
	@Configuration
	@ConditionalOnProperty(prefix = "wsmhz.security.oauth2", name = "tokenStore", havingValue = "jwt", matchIfMissing = true)
	public static class JwtConfig {
		
		@Autowired
		private SecurityProperties securityProperties;

		@Bean
		public TokenStore jwtTokenStore() {
			return new JwtTokenStore(jwtAccessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter jwtAccessTokenConverter(){
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	        converter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
	        return converter;
		}

		@Bean
		@ConditionalOnBean(TokenEnhancer.class)
		public TokenEnhancer jwtTokenEnhancer(){
			return new TokenJwtEnhancer();
		}
		
	}
	
	

}
