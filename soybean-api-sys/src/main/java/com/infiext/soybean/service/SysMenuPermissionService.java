package com.infiext.soybean.service;

import com.infiext.soybean.po.SysMenuPermissionPO;

import java.util.List;

public interface SysMenuPermissionService {
    void resetMenuPermissions(String parentId, List<SysMenuPermissionPO> relations);
}
