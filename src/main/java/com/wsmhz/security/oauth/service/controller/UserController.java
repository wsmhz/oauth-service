package com.wsmhz.security.oauth.service.controller;

import com.wsmhz.common.business.response.ServerResponse;
import com.wsmhz.security.oauth.service.domain.entity.User;
import com.wsmhz.security.oauth.service.domain.vo.UserVo;
import com.wsmhz.security.oauth.service.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * create by tangbj on 2018/5/21
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public ServerResponse<UserVo> select(@PathVariable("id")Long id){
        User user = userService.selectByPrimaryKey(id);
        UserVo userDto = new UserVo();
        BeanUtils.copyProperties(user,userDto);
        return ServerResponse.createBySuccess(userDto);
    }

    @PostMapping("/register")
    public ServerResponse<String> register(@RequestBody User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        Integer result = userService.insertSelective(user);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("注册成功");
        }else{
            return  ServerResponse.createByErrorMessage("注冊失败");
        }
    }


}
