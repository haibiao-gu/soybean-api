package com.infiext.soybean.service;

import com.infiext.soybean.po.SysUserRolePO;

import java.util.List;

public interface SysUserRoleService {
    void resetUserRole(String parentId, List<SysUserRolePO> relations);
}
