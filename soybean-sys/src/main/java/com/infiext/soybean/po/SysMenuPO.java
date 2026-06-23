package com.infiext.soybean.po;

import com.infiext.soybean.domain.BasePO;
import com.infiext.soybean.enums.StatusEnum;
import com.infiext.soybean.enums.YesOrNoEnum;
import com.infiext.soybean.utils.excel.annotation.ExcelField;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 系统菜单 数据表的PO对象
 */
@Table("sys_menu")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysMenuPO extends BasePO<SysMenuPO> {
    /**
     * 状态（1-正常，0-停用）
     */
    @Column(value = "status")
    @ExcelField(value = "状态（1-正常，0-停用）", unique = false, sort = 7, required = true, maxLength = 10)
    private StatusEnum status;

    /**
     * 父菜单ID（0表示根菜单）
     */
    @Column(value = "parent_id")
    @ExcelField(value = "父菜单ID（0表示根菜单）", unique = false, sort = 8, required = true, maxLength = 32)
    private String parentId;

    /**
     * 菜单类型（1:目录 2:菜单）
     */
    @Column(value = "menu_type")
    @ExcelField(value = "菜单类型（1:目录 2:菜单）", unique = false, sort = 9, required = true, maxLength = 10)
    private String menuType;

    /**
     * 菜单名称
     */
    @Column(value = "menu_name")
    @ExcelField(value = "菜单名称", unique = false, sort = 10, required = true, maxLength = 50)
    private String menuName;

    /**
     * 路由名称
     */
    @Column(value = "route_name")
    @ExcelField(value = "路由名称", unique = false, sort = 11, required = true, maxLength = 50)
    private String routeName;

    /**
     * 路由路径
     */
    @Column(value = "route_path")
    @ExcelField(value = "路由路径", unique = false, sort = 12, required = true, maxLength = 500)
    private String routePath;

    /**
     * 组件路径
     */
    @Column(value = "component")
    @ExcelField(value = "组件路径", unique = false, sort = 13, required = false, maxLength = 500)
    private String component;

    /**
     * 图标（iconify图标名或本地图标名）
     */
    @Column(value = "icon")
    @ExcelField(value = "图标（iconify图标名或本地图标名）", unique = false, sort = 14, required = false, maxLength = 64)
    private String icon;

    /**
     * 图标类型（1:iconify图标 2:本地图标）
     */
    @Column(value = "icon_type")
    @ExcelField(value = "图标类型（1:iconify图标 2:本地图标）", unique = false, sort = 15, required = false, maxLength = 10)
    private String iconType;

    /**
     * 国际化key
     */
    @Column(value = "i18n_key")
    @ExcelField(value = "国际化key", unique = false, sort = 16, required = false, maxLength = 50)
    private String i18nKey;

    /**
     * 是否缓存（0:否 1:是）
     */
    @Column(value = "keep_alive")
    @ExcelField(value = "是否缓存（0:否 1:是）", unique = false, sort = 17, required = false, maxLength = 32)
    private YesOrNoEnum keepAlive;

    /**
     * 是否常量路由（0:否 1:是）
     */
    @Column(value = "constant")
    @ExcelField(value = "是否常量路由（0:否 1:是）", unique = false, sort = 18, required = false, maxLength = 32)
    private YesOrNoEnum constant;

    /**
     * 排序号
     */
    @Column(value = "sort_order")
    @ExcelField(value = "排序号", unique = false, sort = 19, required = false, maxLength = 32)
    private Integer sortOrder;

    /**
     * 外链地址
     */
    @Column(value = "href")
    @ExcelField(value = "外链地址", unique = false, sort = 20, required = false, maxLength = 500)
    private String href;

    /**
     * 是否在菜单中隐藏（0:否 1:是）
     */
    @Column(value = "hide_in_menu")
    @ExcelField(value = "是否在菜单中隐藏（0:否 1:是）", unique = false, sort = 21, required = false, maxLength = 32)
    private YesOrNoEnum hideInMenu;

    /**
     * 激活的菜单（用于高亮）
     */
    @Column(value = "active_menu")
    @ExcelField(value = "激活的菜单（用于高亮）", unique = false, sort = 22, required = false, maxLength = 50)
    private String activeMenu;

    /**
     * 是否支持多标签（0:否 1:是）
     */
    @Column(value = "multi_tab")
    @ExcelField(value = "是否支持多标签（0:否 1:是）", unique = false, sort = 23, required = false, maxLength = 32)
    private YesOrNoEnum multiTab;

    /**
     * 在tab中的固定索引
     */
    @Column(value = "fixed_index_in_tab")
    @ExcelField(value = "在tab中的固定索引", unique = false, sort = 24, required = false, maxLength = 32)
    private Integer fixedIndexInTab;

    @Column(ignore = true)
    @RelationOneToMany(selfField = "id", targetField = "menuId")
    private List<SysMenuQueryPO> query;

    @Column(ignore = true)
    @RelationOneToMany(selfField = "id", targetField = "menuId")
    private List<SysMenuPermissionPO> permissions;

    @Column(ignore = true)
    private List<SysMenuPO> children;
}