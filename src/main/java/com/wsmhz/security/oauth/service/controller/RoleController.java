package com.wsmhz.security.oauth.service.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wsmhz.common.business.dto.MybatisPage;
import com.wsmhz.common.business.response.ServerResponse;
import com.wsmhz.security.oauth.service.domain.entity.Resource;
import com.wsmhz.security.oauth.service.domain.entity.Role;
import com.wsmhz.security.oauth.service.service.RoleResourceService;
import com.wsmhz.security.oauth.service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * create by tangbj on 2018/4/28
 */
@RestController
@RequestMapping("/manage/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceService roleResourceService;

    @GetMapping("/{id}")
    public ServerResponse<Role> select(@PathVariable("id")Long id){
        return  ServerResponse.createBySuccess(roleService.selectByPrimaryKey(id));
    }

    @GetMapping("/{id}/resource")
    public ServerResponse<List<Resource>> selectAllResourceByRole(@PathVariable("id")Long id){
        return  ServerResponse.createBySuccess(roleService.selectResourceWithChildByRole(Lists.newArrayList(new Role(id)), null));
    }

    @GetMapping()
    public ServerResponse<List<Role>> selectAll(){
        List<Role> list = roleService.selectAll();
        return  ServerResponse.createBySuccess(list);
    }

    @GetMapping("/page")
    public ServerResponse<MybatisPage> selectAllofPage(@RequestParam(value = "pageNum")Integer pageNum,
                                                       @RequestParam(value = "pageSize")Integer pageSize){
        PageInfo<Role> pageInfo = roleService.selectPageListOrderByDate(pageNum,pageSize,new Role());
        return  ServerResponse.createBySuccess(new MybatisPage<>(pageInfo.getTotal(),pageInfo.getList()));
    }

    /**
     * 分配角色资源
     * @param id    角色id
     * @param resourceIds   资源id，多个资源id 使用逗号相隔
     * @return
     */
    @PostMapping("/{id}/resource")
    public ServerResponse<String> assignResource(@PathVariable("id")Long id,@RequestParam("resourceIds")String resourceIds){
        Integer result = roleResourceService.insertBatch(id,resourceIds);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("分配成功");
        }else{
            return  ServerResponse.createByErrorMessage("分配失败");
        }
    }

    @PostMapping
    public ServerResponse<String> insert(@RequestBody Role role){
        Integer result = roleService.insertSelective(role);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("新增成功");
        }else{
            return  ServerResponse.createByErrorMessage("新增失败");
        }
    }

    @PutMapping
    public ServerResponse<String> update(@RequestBody Role role){
        Integer result = roleService.updateByPrimaryKeySelective(role);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("修改成功");
        }else{
            return  ServerResponse.createByErrorMessage("修改失败");
        }
    }

    @DeleteMapping("/{id}")
    public ServerResponse<String> delete(@PathVariable("id") Long id){
        Integer result = roleService.deleteByPrimaryKey(id);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("删除成功");
        }else{
            return  ServerResponse.createByErrorMessage("删除失败");
        }
    }
}
