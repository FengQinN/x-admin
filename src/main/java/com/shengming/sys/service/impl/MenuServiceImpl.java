package com.shengming.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shengming.sys.entity.Menu;
import com.shengming.sys.mapper.MenuMapper;
import com.shengming.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shengming
 * @since 2023-04-29
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<Menu> getAllMenu() {
        /*查询一级菜单*/
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        /*查询哪些数据的parentId等于0*/
        /*where parentId = 0*/
        wrapper.eq(Menu::getParentId,0);
        List<Menu> menuList = this.list(wrapper);
        /*填充二级菜单*/
        setMenuChildren(menuList);
        return menuList;
    }

    @Override
    public List<Menu> getMenuListByUserId(Integer userId) {
        //一级菜单
        List<Menu> menuList = this.baseMapper.getMenuListByUserId(userId, 0);
        //二级菜单
        setMenuChildrenByUserId(userId, menuList);
        return menuList;
    }

    private void setMenuChildrenByUserId(Integer userId, List<Menu> menuList) {
        if (null != menuList) {
            for (Menu menu : menuList) {
                List<Menu> subMenuList = this.getBaseMapper().getMenuListByUserId(userId, menu.getMenuId());
                menu.setChildren(subMenuList);
                // 递归获取子菜单(必要！)
                setMenuChildrenByUserId(userId,subMenuList);
            }
        }
    }

    private void setMenuChildren(List<Menu> menuList) {
        if (menuList != null) {
            for (Menu menu : menuList) {
                LambdaQueryWrapper<Menu> subWrapper = new LambdaQueryWrapper<>();
                /*查询哪些数据的parentId等于本条数据的menuId*/
                /*where parenId = menuId
                 * 遍历每个menuId
                 * */
                subWrapper.eq(Menu::getParentId,menu.getMenuId());
                List<Menu> subMenuList = this.list(subWrapper);
                /*将查询到的二级菜单装填*/
                menu.setChildren(subMenuList);
                //递归,如果不是叶子节点则重新调用
                setMenuChildren(subMenuList);
            }
        }
    }
}
