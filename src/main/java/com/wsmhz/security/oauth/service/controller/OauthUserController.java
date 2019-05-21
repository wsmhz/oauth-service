package com.wsmhz.security.oauth.service.controller;

import com.wsmhz.common.business.response.ServerResponse;
import com.wsmhz.security.oauth.service.domain.entity.User;
import com.wsmhz.security.oauth.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created By TangBiJing On 2019/5/13
 * Description:
 */
@RestController
public class OauthUserController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
//    @Autowired
//    private UserApi userApi;

    @GetMapping("/test")
    public ServerResponse test() {
        return ServerResponse.createBySuccessMessage("oauth test-success");
    }

    @GetMapping("/userInfo")
    public ServerResponse testUser(Authentication authentication) {
        return ServerResponse.createBySuccess(authentication.getPrincipal());
    }

    @PostMapping("/register")
    public ServerResponse register(@RequestBody @Valid User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        if(userService.insertSelective(user) > 0){
            return ServerResponse.createBySuccessMessage("注册成功");
        }
        return ServerResponse.createBySuccessMessage("注册失败");
    }
}
