package com.infiext.soybean.service;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.po.SysUserPO;
import com.mybatisflex.core.paginate.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysUserService {

    SysUserPO create(SysUserPO po);

    @Transactional
    void createBatch(List<SysUserPO> list);

    SysUserPO update(SysUserPO po);

    void delete(List<String> ids);

    SysUserPO getById(String id);

    Page<SysUserPO> getPage(SysUserPO query, Page<SysUserPO> page, SortParam sort);

    List<SysUserPO> getList(SysUserPO query, SortParam sort);

    String getUserId(String phone, String password);

    List<String> getUserRoleIds(String userId);

    List<String> getRoleList(String userId);

    List<String> getPermissionList(String userId);

    void updatePassword(String userId, String password);
}
