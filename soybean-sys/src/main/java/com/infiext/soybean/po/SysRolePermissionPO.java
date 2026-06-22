package com.infiext.soybean.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色权限关联表 数据表的PO对象
 */
@Table("sys_role_permission")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysRolePermissionPO extends Model<SysRolePermissionPO> {
    /**
     * 角色ID
     */
    @Column(value = "role_id")
    private String roleId;

    /**
     * 权限
     */
    @Column(value = "permission_key")
    private String permissionKey;

}