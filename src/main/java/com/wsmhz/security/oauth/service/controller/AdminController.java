package com.wsmhz.security.oauth.service.controller;

import com.github.pagehelper.PageInfo;
import com.wsmhz.common.business.dto.MybatisPage;
import com.wsmhz.common.business.response.ServerResponse;
import com.wsmhz.security.oauth.service.domain.entity.*;
import com.wsmhz.security.oauth.service.domain.vo.AdminInfoVo;
import com.wsmhz.security.oauth.service.service.AdminService;
import com.wsmhz.security.oauth.service.service.RoleAdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * create by tangbj on 2018/4/28
 */
@RestController
@RequestMapping(value = "/manage/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleAdminService roleAdminService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public ServerResponse<AdminInfoVo> me(@PathVariable("id")Long id){
        AdminInfoVo adminInfoDto = new AdminInfoVo();
        Admin admin = adminService.selectByPrimaryKey(id);
        BeanUtils.copyProperties(admin,adminInfoDto);

        List<Long> roleIdList = new ArrayList<>();
        List<Long> resourceIdList = new ArrayList<>();
        List<Role> roleList = adminService.selectAllRoleByAdmin(new Admin(id));
        for (Role role : roleList) {
            roleIdList.add(role.getId());
        }
        List<Resource> resourceList = adminService.selectMenuTreeByAdmin(new Admin(id), ResourceType.MENU);
        for (Resource resource : resourceList) {
            resourceIdList.add(resource.getId());
        }
        adminInfoDto.setRoleIdList(roleIdList);
        adminInfoDto.setResourceIdList(resourceIdList);
        return  ServerResponse.createBySuccess(adminInfoDto);
    }

    @GetMapping()
//    @JsonView(Admin.AdminSimpleView.class)
    public ServerResponse<List<Admin>> selectAll(){
        List<Admin> list = adminService.selectAll();
        return  ServerResponse.createBySuccess(list);
    }

    @GetMapping("/page")
//    @JsonView(Admin.AdminSimpleView.class)
    public ServerResponse<MybatisPage> selectAllOfPage(@RequestParam("pageNum")Integer pageNum,
                                                       @RequestParam("pageSize")Integer pageSize,
                                                       @RequestParam(value = "username",required = false)String username,
                                                       @RequestParam(value = "status",required = false,defaultValue = "true")boolean status){
        PageInfo<Admin> adminPageInfo = adminService.selectPageListWithSearch(pageNum,pageSize,username,status);
        return  ServerResponse.createBySuccess(new MybatisPage<>(adminPageInfo.getTotal(),adminPageInfo.getList()));
    }

    @GetMapping("/{id}/role")
    public ServerResponse<List<Role>> selectAllRoleByAdmin(@PathVariable("id")Long id){
        return  ServerResponse.createBySuccess(adminService.selectAllRoleByAdmin(new Admin(id)));
    }

    @GetMapping("/{id}/resource")
    public ServerResponse<List<Resource>> selectAllResourceByAdmin(@PathVariable("id")Long id){
        List<Resource> resourceList =  adminService.selectMenuTreeByAdmin(new Admin(id),ResourceType.MENU);
        return  ServerResponse.createBySuccess(resourceList);
    }

    @PutMapping("/{id}")
    public ServerResponse<String> modifyPwd(@RequestParam("oldpassword")String oldpassword,
                                            @RequestParam("password")String password,
                                            @RequestParam("pconfirm")String pconfirm,
                                            @PathVariable("id")Long id){
        Admin admin = adminService.selectByPrimaryKey(id);
        boolean matche = passwordEncoder.matches(oldpassword,admin.getPassword());
        if(matche){
            if(StringUtils.equals(password,pconfirm)){
                String passwordEncode = passwordEncoder.encode(password);
                admin.setPassword(passwordEncode);
                Integer result = adminService.updateByPrimaryKeySelective(admin);
                if(result > 0){
                    return  ServerResponse.createBySuccessMessage("修改成功");
                }else{
                    return  ServerResponse.createByErrorMessage("修改失败");
                }
            }
            return  ServerResponse.createByErrorMessage("密码和确认密码不匹配");
        }
        return  ServerResponse.createByErrorMessage("原密码不正确");
    }

    /**
     * 分配管理员角色
     * @param id    管理员id
     * @param roleIds   角色id，多个角色id 使用逗号相隔
     * @return
     */
    @PostMapping("/{id}/role")
    public ServerResponse<String> assignRole(@PathVariable("id")Long id,@RequestParam("roleIds") String roleIds){
        Integer result = roleAdminService.insertBatch(id,roleIds);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("分配成功");
        }else{
            return  ServerResponse.createByErrorMessage("分配失败");
        }
    }

    @PostMapping
    public ServerResponse<String> insert(@RequestBody Admin admin){
        String encodePassword =passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodePassword);
        Integer result = adminService.insertSelective(admin);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("新增成功");
        }else{
            return  ServerResponse.createByErrorMessage("新增失败");
        }
    }

    @PutMapping
    public ServerResponse<String> update(@RequestBody Admin admin){
        Integer result = adminService.updateByPrimaryKeySelective(admin);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("修改成功");
        }else{
            return  ServerResponse.createByErrorMessage("修改失败");
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ServerResponse<String> delete(@PathVariable("id") Long id){
        Integer result = adminService.deleteByPrimaryKey(id);
        roleAdminService.deleteByExample(new RoleAdmin(),"adminId",String.valueOf(id));
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("删除成功");
        }else{
            return  ServerResponse.createByErrorMessage("删除失败");
        }
    }


}
