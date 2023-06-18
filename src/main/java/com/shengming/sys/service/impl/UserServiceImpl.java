package com.shengming.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.shengming.common.utils.JwtUtil;
import com.shengming.sys.entity.Menu;
import com.shengming.sys.entity.User;
import com.shengming.sys.entity.UserRole;
import com.shengming.sys.mapper.UserMapper;
import com.shengming.sys.mapper.UserRoleMapper;
import com.shengming.sys.service.IMenuService;
import com.shengming.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shengming
 * @since 2023-04-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private IMenuService menuService;
   /* @Override
    public Map<String, Object> login(User user) {
        //查询，根据用户名和密码
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        wrapper.eq(User::getPassword, user.getPassword());
        //第一步
        User loginUser = this.baseMapper.selectOne(wrapper);
        //查到结果不为空，则生成一个token，并将用户信息存如Redis中
        if (loginUser != null) {
            //暂时使用UUID,终极方案是jwt
            String key =  "user:" + UUID.randomUUID();
            //存入Redis
            //在Linux中的Redis服务记得把防火墙关了
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);
            //返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token",key);
            return data;
        }
        return null;
    }*/

    /*加密密码后的登录逻辑*/
   /*@Override
   public Map<String, Object> login(User user) {
       //查询，根据用户名查询
       LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
       wrapper.eq(User::getUsername, user.getUsername());
       //第一步
       User loginUser = this.baseMapper.selectOne(wrapper);
       //查到结果不为空，并且密码和传入的密码是匹配的，则生成一个token，并将用户信息存如Redis中
       if (loginUser != null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())) {
           //暂时使用UUID,终极方案是jwt
           String key =  "user:" + UUID.randomUUID();
           //存入Redis
           //在Linux中的Redis服务记得把防火墙关了
           loginUser.setPassword(null);
           redisTemplate.opsForValue().set(key,loginUser,30, TimeUnit.MINUTES);
           //返回数据
           HashMap<String, Object> data = new HashMap<>();
           data.put("token",key);
           return data;
       }
       return null;
   }*/

    /*用户登录*/
    @Override
    public Map<String, Object> login(User user) {
        //查询，根据用户名查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        //第一步查询到user
        User loginUser = this.baseMapper.selectOne(wrapper);
        //查到结果不为空，并且数据库中的加密密码与用户输入的密码匹配则生成一个token
        if (loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
            loginUser.setPassword(null);
            //创建jwt
            String token = jwtUtil.createToken(loginUser);
            //返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", token);
            return data;
        }
        return null;
    }


    @Override
    public Map<String, Object> getUserInfo(String token) {
        //根据token从Redis中获取用户信息
        //Object o = redisTemplate.opsForValue().get(token);
        User user = null;
        try {
            user = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (user != null) {
            //Redis中的取出的数据反序列化
            //User user = JSON.parseObject(JSON.toJSONString(o), User.class);
            //将User的数据封装到Map里
            HashMap<String, Object> data = new HashMap<>();
            data.put("name", user.getUsername());
            data.put("avatar", user.getAvatar());
            List<String> roleList = this.baseMapper.getRoleNameByUserId(user.getId());
            data.put("roles", roleList);
            //根据登录用户信息获取菜单列表
            List<Menu> menuList= menuService.getMenuListByUserId(user.getId());
            data.put("menuList",menuList);
            return data;
        }
        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }

    @Override
    //涉及多表，使用事务注解
    @Transactional
    public void addUser(User user) {
        //新增数据到用户表
        this.baseMapper.insert(user);
        //新增数据到用户-角色表
        List<Integer> roleIdList = user.getRoleIdList();
        if (roleIdList != null) {
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    @Transactional
    public User getUserById(Integer id) {
        //查用用户表信息
        User user = this.baseMapper.selectById(id);
        //查询用户角色表信息
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        List<Integer> roleIdList = userRoleMapper.selectList(wrapper).stream().map(UserRole::getRoleId).collect(Collectors.toList());
        user.setRoleIdList(roleIdList);
        return user;
    }

    @Override
    public void updateUser(User user) {
        //修改用户表
        this.baseMapper.updateById(user);
        //根据用户修改用户-角色表
        //第一步,根据userId删除用户-角色表的信息
        userRoleMapper.deleteById(user.getId());
        //第二步，插入新数据
        List<Integer> roleIdList = user.getRoleIdList();
        if (null != roleIdList && !roleIdList.isEmpty()) {
            for (Integer roleId : roleIdList) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        } else {
            throw new RuntimeException("UserController中updateUser()中roleIdList为NUll");
        }
    }
}
