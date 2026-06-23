package com.infiext.soybean.service;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.po.SysRolePO;
import com.mybatisflex.core.paginate.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysRoleService {
    @Transactional
    SysRolePO create(SysRolePO po);

    @Transactional
    void createBatch(List<SysRolePO> list);

    @Transactional
    SysRolePO update(SysRolePO po);

    @Transactional
    void deleteByIds(List<String> ids);

    SysRolePO getById(String id);

    Page<SysRolePO> getPage(SysRolePO query, Page<SysRolePO> page, SortParam sort);

    List<SysRolePO> getList(SysRolePO query, SortParam sort);

    List<String> getRoleMenuIds(String roleId);

    List<String> getRoleMenuIds(List<String> roleIds);
}
