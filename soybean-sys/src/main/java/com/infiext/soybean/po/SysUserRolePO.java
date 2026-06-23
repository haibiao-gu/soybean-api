package com.infiext.soybean.po;

import com.infiext.soybean.utils.excel.annotation.ExcelField;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户角色关联表 数据表的PO对象
 */
@Table("sys_user_role")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysUserRolePO extends Model<SysUserRolePO> {
    /**
     * 用户ID
     */
    @Column(value = "user_id")
    @ExcelField(value = "用户ID", unique = false, sort = 0, required = true, maxLength = 32)
    private String userId;

    /**
     * 角色ID
     */
    @Column(value = "role_id")
    @ExcelField(value = "角色ID", unique = false, sort = 1, required = true, maxLength = 32)
    private String roleId;

}