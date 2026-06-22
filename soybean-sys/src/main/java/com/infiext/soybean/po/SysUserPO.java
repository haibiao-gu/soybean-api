package com.infiext.soybean.po;

import com.infiext.soybean.domain.BasePO;
import com.infiext.soybean.enums.StatusEnum;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户表 数据表的PO对象
 */
@Table("sys_user")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysUserPO extends BasePO<SysUserPO> {
    /**
     * 用户姓名
     */
    @Column(value = "user_name")
    private String userName;

    /**
     * 用户手机号码
     */
    @Column(value = "user_phone")
    private String userPhone;

    /**
     * 用户头像
     */
    @Column(value = "user_avatar")
    private String userAvatar;

    /**
     * 登录密码
     */
    @Column(value = "password")
    private String password;

    /**
     * 状态（1-正常，0-停用）
     */
    @Column(value = "status")
    private StatusEnum status;

    @Column(ignore = true)
    @RelationOneToMany(selfField = "id", targetField = "userId")
    private List<SysUserRolePO> roles;
}