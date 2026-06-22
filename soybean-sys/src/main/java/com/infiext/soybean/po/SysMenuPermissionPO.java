package com.infiext.soybean.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 菜单权限 数据表的PO对象
 */
@Table("sys_menu_permission")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysMenuPermissionPO extends Model<SysMenuPermissionPO> {
    /**
     * 菜单ID
     */
    @Column(value = "menu_id")
    private String menuId;

    /**
     * 权限
     */
    @Column(value = "key")
    private String key;

    /**
     * 描述
     */
    @Column(value = "value")
    private String value;

}