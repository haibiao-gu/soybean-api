package com.infiext.soybean.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色菜单关联表 数据表的PO对象
 */
@Table("sys_role_menu")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysRoleMenuPO extends Model<SysRoleMenuPO> {
    /**
     * 角色ID
     */
    @Column(value = "role_id")
    private String roleId;

    /**
     * 菜单ID
     */
    @Column(value = "menu_id")
    private String menuId;

}