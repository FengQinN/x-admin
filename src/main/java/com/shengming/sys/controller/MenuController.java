package com.shengming.sys.controller;

import com.shengming.common.vo.Result;
import com.shengming.sys.entity.Menu;
import com.shengming.sys.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/menu")
@Api(tags = {"菜单接口列表"})
public class MenuController {
    @Autowired
    private IMenuService menuService;
    @GetMapping("/list")
    @ApiOperation("查询所有菜单信息")
    public Result<List<Menu>> getAllMenu(){
        List<Menu> menuList = menuService.getAllMenu();
        return Result.success(menuList);
    }
}
