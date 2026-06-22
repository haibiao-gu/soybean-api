package com.infiext.soybean.service;

import com.infiext.soybean.po.SysMenuQueryPO;

import java.util.List;

public interface SysMenuQueryService {
    void resetMenuQuery(String parentId, List<SysMenuQueryPO> relations);
}
