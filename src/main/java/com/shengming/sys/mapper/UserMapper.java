package com.shengming.sys.mapper;

import com.shengming.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shengming
 * @since 2023-04-29
 */
public interface UserMapper extends BaseMapper<User> {
    List<String> getRoleNameByUserId(@Param("userId") Integer userId);
}
