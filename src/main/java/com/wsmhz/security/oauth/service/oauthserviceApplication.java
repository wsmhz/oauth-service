package com.wsmhz.security.oauth.service;

import org.springframework.boot.SpringApplication;

import com.wsmhz.common.business.annotation.WsmhzMicroServiceApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * Created By TangBiJing
 * Description: 
 */
@EnableFeignClients("com.wsmhz")
@SpringCloudApplication
@WsmhzMicroServiceApplication
public class OauthserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthserviceApplication.class, args);
    }
}