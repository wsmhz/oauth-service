package com.wsmhz.security.oauth.service.controller;

import com.github.pagehelper.PageInfo;
import com.wsmhz.common.business.dto.MybatisPage;
import com.wsmhz.common.business.response.ServerResponse;
import com.wsmhz.security.oauth.service.domain.entity.Resource;
import com.wsmhz.security.oauth.service.domain.entity.RoleResource;
import com.wsmhz.security.oauth.service.service.ResourceService;
import com.wsmhz.security.oauth.service.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * create by tangbj on 2018/4/28
 */
@RestController
@RequestMapping("/manage/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleResourceService roleResourceService;

    @GetMapping("/{id}")
    public ServerResponse<Resource> select(@PathVariable("id")Long id){
        return  ServerResponse.createBySuccess(resourceService.selectByPrimaryKey(id));
    }

    @GetMapping()
    public ServerResponse<List<Resource>> selectAll(){
        return  ServerResponse.createBySuccess(resourceService.selectAll());
    }

    @GetMapping("/page")
    public ServerResponse<MybatisPage> selectAll(@RequestParam(value = "pageNum")Integer pageNum,
                                                 @RequestParam(value = "pageSize")Integer pageSize){
        PageInfo<Resource> pageInfo = resourceService.selectPageListOrderByDate(pageNum,pageSize,new Resource());
        return  ServerResponse.createBySuccess(new MybatisPage<>(pageInfo.getTotal(),pageInfo.getList()));
    }

    @PostMapping
    public ServerResponse<String> insert(@RequestBody Resource resource){
        Integer result = resourceService.insertSelective(resource);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("新增成功");
        }else{
            return  ServerResponse.createByErrorMessage("新增失败");
        }
    }

    @PutMapping
    public ServerResponse<String> update(@RequestBody Resource resource){
        Integer result = resourceService.updateByPrimaryKeySelective(resource);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("修改成功");
        }else{
            return  ServerResponse.createByErrorMessage("修改失败");
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ServerResponse<String> delete(@PathVariable("id") Long id){
        Integer result = resourceService.deleteByPrimaryKey(id);
        roleResourceService.deleteByExample(new RoleResource(),"resource_id",id);
        if(result > 0){
            return  ServerResponse.createBySuccessMessage("删除成功");
        }else{
            return  ServerResponse.createByErrorMessage("删除失败");
        }
    }
}
