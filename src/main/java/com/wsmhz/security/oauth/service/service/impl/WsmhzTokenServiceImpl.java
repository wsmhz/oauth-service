package com.wsmhz.security.oauth.service.service.impl;

import com.wsmhz.security.oauth.service.service.WsmhzTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

/**
 * Created By TangBiJing On 2019/5/13
 * Description:
 */
@Service
public class WsmhzTokenServiceImpl implements WsmhzTokenService {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public boolean revokeToken(String tokenValue) {

        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null) {
            return false;
        }
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        tokenStore.removeAccessToken(accessToken);
        return true;
    }
}
