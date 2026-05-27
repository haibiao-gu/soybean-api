package com.infiext.soybean.po;

import com.infiext.soybean.domain.BasePO;
import com.infiext.soybean.enums.StatusEnum;
import com.infiext.soybean.enums.YesOrNoEnum;
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
    private StatusEnum status;

    /**
     * 父菜单ID（0表示根菜单）
     */
    @Column(value = "parent_id")
    private String parentId;

    /**
     * 菜单类型（1:目录 2:菜单）
     */
    @Column(value = "menu_type")
    private String menuType;

    /**
     * 菜单名称
     */
    @Column(value = "menu_name")
    private String menuName;

    /**
     * 路由名称
     */
    @Column(value = "route_name")
    private String routeName;

    /**
     * 路由路径
     */
    @Column(value = "route_path")
    private String routePath;

    /**
     * 组件路径
     */
    @Column(value = "component")
    private String component;

    /**
     * 图标（iconify图标名或本地图标名）
     */
    @Column(value = "icon")
    private String icon;

    /**
     * 图标类型（1:iconify图标 2:本地图标）
     */
    @Column(value = "icon_type")
    private String iconType;

    /**
     * 国际化key
     */
    @Column(value = "i18n_key")
    private String i18nKey;

    /**
     * 是否缓存（0:否 1:是）
     */
    @Column(value = "keep_alive")
    private YesOrNoEnum keepAlive;

    /**
     * 是否常量路由（0:否 1:是）
     */
    @Column(value = "constant")
    private YesOrNoEnum constant;

    /**
     * 排序号
     */
    @Column(value = "sort_order")
    private Integer sortOrder;

    /**
     * 外链地址
     */
    @Column(value = "href")
    private String href;

    /**
     * 是否在菜单中隐藏（0:否 1:是）
     */
    @Column(value = "hide_in_menu")
    private YesOrNoEnum hideInMenu;

    /**
     * 激活的菜单（用于高亮）
     */
    @Column(value = "active_menu")
    private String activeMenu;

    /**
     * 是否支持多标签（0:否 1:是）
     */
    @Column(value = "multi_tab")
    private YesOrNoEnum multiTab;

    /**
     * 在tab中的固定索引
     */
    @Column(value = "fixed_index_in_tab")
    private Integer fixedIndexInTab;

    @Column(ignore = true)
    @RelationOneToMany(selfField = "id", targetField = "menuId")
    private List<SysMenuQueryPO> query;

    @Column(ignore = true)
    private List<SysMenuPO> children;
}