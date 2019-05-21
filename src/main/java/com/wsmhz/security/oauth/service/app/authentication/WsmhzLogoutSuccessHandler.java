package com.wsmhz.security.oauth.service.app.authentication;

import com.wsmhz.common.business.response.ServerResponse;
import com.wsmhz.common.business.utils.JsonUtil;
import com.wsmhz.security.oauth.service.service.WsmhzTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * create by tangbj on 2018/4/6
 * 自定义退出登陆处理器
 */
@Slf4j
@Component
public class WsmhzLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private WsmhzTokenService wsmhzTokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String authorization = request.getHeader("Authorization");
        response.setContentType("application/json;charset=UTF-8");
        if (authorization == null || ! authorization.contains("bearer")) {
            log.error("未携带身份信息，无法退出");
            response.getWriter().write(JsonUtil.objToString(ServerResponse.createByErrorMessage("未携带身份信息，无法退出")));
            return;
        }
        String token = authorization.substring("Bearer".length() + 1);
        wsmhzTokenService.revokeToken(token);
        log.info("退出成功");
        response.getWriter().write(JsonUtil.objToString(ServerResponse.createBySuccessMessage("退出成功")));
    }
}
