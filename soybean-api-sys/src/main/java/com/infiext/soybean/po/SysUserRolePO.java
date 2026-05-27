package com.infiext.soybean.po;

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
    private String userId;

    /**
     * 角色ID
     */
    @Column(value = "role_id")
    private String roleId;

}