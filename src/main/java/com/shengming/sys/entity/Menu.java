package com.shengming.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author shengming
 * @since 2023-04-29
 */
@TableName("x_menu")
@Data
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;
    private String component;
    private String path;
    private String redirect;
    private String name;
    private String title;
    private String icon;
    private Integer parentId;
    private String isLeaf;
    private Boolean hidden;

    @TableField(exist = false)//屏蔽非数据库字段
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    /*子菜单*/
    private List<Menu> children;
    @TableField(exist = false)
    /*meta*/
    private Map<String,Object> meta;

    public Map<String, Object> getMeta() {
        this.meta = new HashMap<>();
        meta.put("title",this.title);
        meta.put("iocn",this.icon);
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
}
