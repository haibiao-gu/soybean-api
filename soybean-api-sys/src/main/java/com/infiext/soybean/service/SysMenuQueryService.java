package com.infiext.soybean.service;

import com.infiext.soybean.po.SysMenuQueryPO;

import java.util.List;

public interface SysMenuQueryService {
    void resetMenuQuery(String menuId, List<SysMenuQueryPO> query);
}
