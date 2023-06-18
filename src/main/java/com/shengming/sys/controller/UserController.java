package com.shengming.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shengming.common.vo.Result;
import com.shengming.sys.entity.User;
import com.shengming.sys.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shengming
 * @since 2023-04-29
 */

@RestController
@RequestMapping("/user")
@Api(tags = {"用户接口列表"})
//@CrossOrigin 跨域处理
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*查询所有用户列表*/
    @GetMapping("/all")
    public Result<List<User>> getAllUser(){
        List<User> list = userService.list();
        return  Result.success(list,"查询成功");
    }

    /*用户登录*/
    @PostMapping("/login")
    @ApiOperation("用户登录接口")
    public Result<Map<String,Object>> login(@RequestBody User user){
        Map<String,Object> data = userService.login(user);
        if (data != null){
            return Result.success(data);
        }
        return Result.fail(20002,"用户名或密码错误");
    }

    /*获取info信息*/
    @GetMapping("/info")
    @ApiOperation("获取info信息")
    public Result<Map<String,Object>> getUserInfo(@RequestParam("token") String token){
        //根据token从Redis中获取用户信息
        Map<String,Object> data =userService.getUserInfo(token);
        if (data != null){
            return Result.success(data);
        }
        return Result.fail(20003,"token无效");
    }

    /*注销*/
    @PostMapping("/logout")
    @ApiOperation("注销")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success();
    }

    /*用户管理结果查询结果请求*/
    @GetMapping("/list")
    @ApiOperation("用户管理结果查询结果请求")
    public Result<Map<String,Object>> selectUser(@RequestParam(value = "username",required = false) String username,
                                                 @RequestParam(value = "phone",required = false) String phone,
                                                 @RequestParam(value = "pageNo") Long pageNo,
                                                 @RequestParam(value = "pageSize") Long pageSize){
        /*查询用户*/
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        //当username字段不为串时则进行此查询
        queryWrapper.eq(StringUtils.hasLength(username),User::getUsername,username);
        queryWrapper.eq(StringUtils.hasLength(phone),User::getPhone,phone);
        /*根据ID排序*/
        queryWrapper.orderByDesc(User::getId);
        /*分页查询*/
        Page<User> page = new Page<>(pageNo, pageSize);
        userService.page(page,queryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",page.getTotal());
        map.put("rows",page.getRecords());
        return Result.success(map);
    }

    /*新增用户请求*/
    @PostMapping
    @ApiOperation("新增用户请求")
    public Result<?> addUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return Result.success("添加成功");
    }

    /*修改用户信息请求*/
    @PutMapping
    @ApiOperation("修改用户信息请求")
    public Result<?> updateUser(@RequestBody User user){
        user.setPassword(null);
        userService.updateUser(user);
        return Result.success("修改成功");
    }

    /*根据Id查询用户信息*/
    @ApiOperation("根据Id查询用户信息")
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id){
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /*删除用户信息*/
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户信息")
    public Result<?> deleteUserById(@PathVariable("id") Integer id){
        /*是真删除还是假删除*/
        /*本次删除为逻辑删除即为假删除*/
        userService.removeById(id);
        return Result.success("删除成功");
    }
}
