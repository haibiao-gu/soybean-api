package com.infiext.soybean.service;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.po.SysMenuPO;
import com.mybatisflex.core.paginate.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SysMenuService {
    @Transactional
    SysMenuPO create(SysMenuPO po);

    @Transactional
    void createBatch(List<SysMenuPO> list);

    @Transactional
    SysMenuPO update(SysMenuPO po);

    @Transactional
    void deleteByIds(List<String> ids);

    SysMenuPO getById(String id);

    Page<SysMenuPO> getPage(SysMenuPO query, Page<SysMenuPO> page, SortParam sort);

    List<SysMenuPO> getList(SysMenuPO query, SortParam sort);

    @Transactional
    void resetSortOrder(List<String> ids);

    String getVersion();

    List<SysMenuPO> getCachedList();

    List<SysMenuPO> tree();

    List<String> allPages();
}
