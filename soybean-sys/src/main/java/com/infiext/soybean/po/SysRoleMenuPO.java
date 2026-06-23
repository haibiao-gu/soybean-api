package com.infiext.soybean.po;

import com.infiext.soybean.utils.excel.annotation.ExcelField;
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
    @ExcelField(value = "角色ID", unique = false, sort = 0, required = true, maxLength = 32)
    private String roleId;

    /**
     * 菜单ID
     */
    @Column(value = "menu_id")
    @ExcelField(value = "菜单ID", unique = false, sort = 1, required = true, maxLength = 32)
    private String menuId;

}