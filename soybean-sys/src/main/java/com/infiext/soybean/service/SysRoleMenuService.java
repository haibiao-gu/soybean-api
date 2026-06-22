package com.infiext.soybean.service;

import com.infiext.soybean.po.SysRoleMenuPO;

import java.util.List;

public interface SysRoleMenuService {
    void resetRoleMenus(String parentId, List<SysRoleMenuPO> relations);
}
