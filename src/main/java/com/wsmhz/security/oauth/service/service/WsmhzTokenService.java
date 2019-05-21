package com.wsmhz.security.oauth.service.service;

/**
 * Created By TangBiJing On 2019/5/13
 * Description:
 */
public interface WsmhzTokenService {

    boolean revokeToken(String tokenValue);
}
