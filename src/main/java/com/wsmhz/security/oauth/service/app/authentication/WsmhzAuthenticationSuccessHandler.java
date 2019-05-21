package com.wsmhz.security.oauth.service.app.authentication;

import com.wsmhz.common.business.response.ServerResponse;
import com.wsmhz.common.business.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * create by tangbj on 2018/4/7
 * 自定义APP验证成功处理器
 */
@Component("wsmhzAuthenticationSuccessHandler")
public class WsmhzAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private AuthorizationServerTokenServices authorizationServerTokenServices;
	@Autowired
	private PasswordEncoder passwordEncoder;

//	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {

		logger.info("登录成功");

		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Basic ")) {
			throw new UnapprovedClientAuthenticationException("请求头中无client信息");
		}

		String[] tokens = extractAndDecodeHeader(header, request);
		assert tokens.length == 2;

		String clientId = tokens[0];
		String clientSecret = tokens[1];

		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

		if (clientDetails == null) {
			throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
		} else if ( ! passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
			throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
		}
		//custom表示自定义的授权模式
		TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");

		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

		OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(JsonUtil.objToString(ServerResponse.createBySuccess(token)));

	}

	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}

//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//										Authentication authentication) throws IOException, ServletException {
//
//		logger.info("登录成功");
//
//		String header = request.getHeader("Authorization");
//
//		if (header == null || !header.startsWith("Basic ")) {
//			throw new UnapprovedClientAuthenticationException("请求头中无client信息");
//		}
//
//		String[] tokens = extractAndDecodeHeader(header, request);
//		assert tokens.length == 2;
//
//		String clientId = tokens[0];
//		String clientSecret = tokens[1];
//
//		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
//
//		if (clientDetails == null) {
//			throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
//		} else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
//			throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
//		}
//		//custom表示自定义的授权模式
//		TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");
//
//		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
//
//		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
//
//		OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
//
//		response.setContentType("application/json;charset=UTF-8");
//		response.getWriter().write(JsonUtil.objToString(token));
//
//	}
//
//	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
//
//		byte[] base64Token = header.substring(6).getBytes("UTF-8");
//		byte[] decoded;
//		try {
//			decoded = Base64.decode(base64Token);
//		} catch (IllegalArgumentException e) {
//			throw new BadCredentialsException("Failed to decode basic authentication token");
//		}
//
//		String token = new String(decoded, "UTF-8");
//
//		int delim = token.indexOf(":");
//
//		if (delim == -1) {
//			throw new BadCredentialsException("Invalid basic authentication token");
//		}
//		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
//	}

}
