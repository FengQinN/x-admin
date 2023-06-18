package com.shengming.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shengming.sys.entity.Role;
import com.shengming.sys.entity.RoleMenu;
import com.shengming.sys.mapper.RoleMapper;
import com.shengming.sys.mapper.RoleMenuMapper;
import com.shengming.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shengming
 * @since 2023-04-29
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional
    public void addRole(Role role) {
        //写入角色表
        this.baseMapper.insert(role);
        //写入角色-菜单表
        if (null != role.getMenuIdList() && !role.getMenuIdList().isEmpty()) {
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    public Role getRoleById(Integer roleId) {
        //查询到角色表中的数据
        Role role = this.baseMapper.selectById(roleId);
        //关联查询,查询menu表中的叶子节点
        List<Integer> menuIdList = roleMenuMapper.getMenuIdByRoleId(roleId);
        role.setMenuIdList(menuIdList);
        return role;
    }

    @Override
    //由于此方法设计到多表，因此需要标记事务注解
    @Transactional
    public void updateRole(Role role) {
        //修改角色表
        this.baseMapper.updateById(role);
        //根据roleId删除role_menu表中的数据
        //记得修改泛型
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(wrapper);
        //新增权限数据,一条一条的插入数据
        //写入角色-菜单表
        if (null != role.getMenuIdList() && !role.getMenuIdList().isEmpty()) {
            for (Integer menuId : role.getMenuIdList()) {
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    //涉及多表，加上事务注解
    @Transactional
    public void deleteRoleById(Integer roleId) {
        //删除角色表中的数据
        this.baseMapper.deleteById(roleId);
        //删除角色-菜单表中的数据
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);
    }
}
