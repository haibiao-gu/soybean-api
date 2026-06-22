package com.infiext.soybean.service;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.po.SysUserPO;
import com.mybatisflex.core.paginate.Page;

import java.util.List;

public interface SysUserService {

    SysUserPO createSysUser(SysUserPO po);

    SysUserPO updateSysUser(SysUserPO po);

    void deleteSysUser(List<String> ids);

    SysUserPO getSysUserById(String id);

    Page<SysUserPO> getSysUserPage(SysUserPO query, Page<SysUserPO> page, SortParam sort);

    List<SysUserPO> getSysUserList(SysUserPO query, SortParam sort);

    String getUserId(String phone, String password);

    List<String> getUserRoleIds(String userId);

    List<String> getRoleList(String userId);

    List<String> getPermissionList(String userId);

    void updatePassword(String userId, String password);
}
