package com.wsmhz.security.oauth.service.controller;

import com.wsmhz.common.business.response.ResponseCode;
import com.wsmhz.common.business.response.ServerResponse;
import com.wsmhz.security.core.properties.SecurityConstants;
import com.wsmhz.security.core.social.SocialController;
import com.wsmhz.security.core.social.SocialUserInfo;
import com.wsmhz.security.oauth.service.app.social.AppSingUpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * create by tangbj on 2018/4/8
 */
@Slf4j
@RestController
public class AppSecurityController extends SocialController {

	private RequestCache requestCache = new HttpSessionRequestCache();//http请求缓存

	@Resource
	private ProviderSignInUtils providerSignInUtils;
	
	@Autowired
	private AppSingUpUtils appSingUpUtils;

	/**
	 * 当需要身份认证时，跳转到这里
	 */
	@RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ServerResponse<String> requireAuthentication(HttpServletRequest request, HttpServletResponse response){
		SavedRequest savedRequest = requestCache.getRequest(request,response);
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			log.info("未登录引发跳转的请求是:" + targetUrl);
		}
		return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
	}

	/**
	 * 需要注册时跳到这里，返回401和用户信息给前端
	 */
	@GetMapping(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
		appSingUpUtils.saveConnectionData(new ServletWebRequest(request), connection.createData());
		return buildSocialUserInfo(connection);
	}

	/**
	 * 授权码模式(无页面)，系统返回的路径url，跳到这里再返回code给前端
	 */
	@GetMapping(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_CODE)
	public ServerResponse getAuthorizationCode(HttpServletRequest request) {
		String code = request.getParameter("code");
		return ServerResponse.createBySuccess(code);
	}

}
