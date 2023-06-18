package com.shengming.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shengming.common.vo.Result;
import com.shengming.sys.entity.Role;
import com.shengming.sys.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shengming
 * @since 2023-04-29
 */
@RestController
@RequestMapping("/role")
@Api(tags = {"角色接口列表"})
public class RoleController {
    @Autowired
    private IRoleService roleService;

    /*查询每页所有角色信息*/
    @ApiOperation("查询每页所有角色信息")
    @GetMapping("/list")
    public Result<Map<String, Object>> selectRole(@RequestParam(value = "roleName", required = false) String roleName,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize) {
        /*条件构造器*/
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        /*要查询的条件*/
        lambdaQueryWrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName,roleName);
        /*根据Id排序*/
        lambdaQueryWrapper.orderByAsc(Role::getRoleId);
        /*创建分页查询条件*/
        Page<Role> page = new Page<>(pageNo,pageSize);
        roleService.page(page,lambdaQueryWrapper);
        /*返回结果*/
        HashMap<String, Object> map = new HashMap<>();
        /*返回查询总数*/
        map.put("total",page.getTotal());
        /*放回查询结果集*/
        map.put("rows",page.getRecords());
        /*return*/
        return Result.success(map);
    }

    /*新增角色请求*/
    @ApiOperation("新增角色请求")
    @PostMapping
    public Result<?> addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return Result.success("新增角色成功！");
    }

    /*修改角色信息请求*/
    @PutMapping
    @ApiOperation("修改角色信息请求")
    public Result<?> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success("修改角色成功！");
    }

    /*根据Id查询角色信息*/
    @GetMapping("/{id}")
    @ApiOperation("根据Id查询角色信息")
    public Result<Role> getRoleById(@PathVariable("id") Integer roleId) {
        Role role = roleService.getRoleById(roleId);
        return Result.success(role);
    }

    /*删除用户信息*/
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户信息")
    public Result<?> deleteRoleById(@PathVariable("id") Integer roleId) {
        roleService.deleteRoleById(roleId);
        return Result.success("删除成功!");
    }

    //查询所有角色的角色名称
    @GetMapping("/getAllRole")
    @ApiOperation("查询所有角色的角色名称")
    public Result<List<Role>> getAllRole(){
        return Result.success(roleService.list());
    }
}
