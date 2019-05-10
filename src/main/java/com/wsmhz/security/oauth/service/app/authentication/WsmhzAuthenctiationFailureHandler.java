package com.wsmhz.security.oauth.service.app.authentication;

import com.wsmhz.common.business.response.ServerResponse;
import com.wsmhz.common.business.utils.JsonUtil;
import com.wsmhz.security.core.properties.LoginResponseType;
import com.wsmhz.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * create by tangbj on 2018/4/7
 * 自定义APP验证错误处理器
 */
@Component("wsmhzAuthenctiationFailureHandler")
public class WsmhzAuthenctiationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		logger.info("登录失败");
        if(securityProperties.getBrowser().getLoginResponseType().equals(LoginResponseType.JSON)){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JsonUtil.objToString(ServerResponse.createByErrorMessage(exception.getMessage())));
        }else{
            super.onAuthenticationFailure(request,response,exception);
        }





	}

}
