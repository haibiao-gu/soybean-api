package com.infiext.soybean.service;

import com.infiext.soybean.po.SysRolePermissionPO;

import java.util.List;

public interface SysRolePermissionService {
    void resetRolePermissions(String parentId, List<SysRolePermissionPO> relations);
}
