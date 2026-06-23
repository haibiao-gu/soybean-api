package com.infiext.soybean.po;

import com.infiext.soybean.domain.BasePO;
import com.infiext.soybean.utils.excel.annotation.ExcelField;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 系统角色 数据表的PO对象
 */
@Table("sys_role")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysRolePO extends BasePO<SysRolePO> {
    /**
     * 角色名称
     */
    @Column(value = "role_name")
    @ExcelField(value = "角色名称", unique = false, sort = 7, required = false, maxLength = 50)
    private String roleName;

    /**
     * 角色编码
     */
    @Column(value = "role_code")
    @ExcelField(value = "角色编码", unique = false, sort = 8, required = true, maxLength = 64)
    private String roleCode;

    /**
     * 角色描述
     */
    @Column(value = "description")
    @ExcelField(value = "角色描述", unique = false, sort = 9, required = false, maxLength = 500)
    private String description;

    @Column(ignore = true)
    @RelationOneToMany(selfField = "id", targetField = "roleId")
    private List<SysRoleMenuPO> menus;
    
    @Column(ignore = true)
    @RelationOneToMany(selfField = "id", targetField = "roleId")
    private List<SysRolePermissionPO> permissions;

}