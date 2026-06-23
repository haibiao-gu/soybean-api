package com.infiext.soybean.po;

import com.infiext.soybean.domain.BasePO;
import com.infiext.soybean.enums.StatusEnum;
import com.infiext.soybean.utils.excel.annotation.ExcelField;
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
    @ExcelField(value = "用户姓名", unique = false, sort = 7, required = true, maxLength = 50)
    private String userName;

    /**
     * 用户手机号码
     */
    @Column(value = "user_phone")
    @ExcelField(value = "用户手机号码", unique = false, sort = 8, required = true, maxLength = 64)
    private String userPhone;

    /**
     * 用户电子邮箱
     */
    @Column(value = "user_email")
    @ExcelField(value = "用户电子邮箱", unique = false, sort = 9, required = false, maxLength = 500)
    private String userEmail;

    /**
     * 用户头像
     */
    @Column(value = "user_avatar")
    @ExcelField(value = "用户头像", unique = false, sort = 10, required = false, maxLength = 500)
    private String userAvatar;

    /**
     * 登录密码
     */
    @Column(value = "password")
    @ExcelField(value = "登录密码", unique = false, sort = 11, required = false, maxLength = 64)
    private String password;

    /**
     * 状态（1-正常，0-停用）
     */
    @Column(value = "status")
    @ExcelField(value = "状态（1-正常，0-停用）", unique = false, sort = 12, required = true, maxLength = 10)
    private StatusEnum status;

    @Column(ignore = true)
    @RelationOneToMany(selfField = "id", targetField = "userId")
    private List<SysUserRolePO> roles;

    @Column(ignore = true)
    private String roleCode;
}