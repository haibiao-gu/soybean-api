package com.infiext.soybean.service.impl;

import com.infiext.soybean.mapper.SysMenuQueryMapper;
import com.infiext.soybean.po.SysMenuQueryPO;
import com.infiext.soybean.service.SysMenuQueryService;
import com.infiext.soybean.utils.RelationResetHandler;
import com.infiext.soybean.utils.RelationResetService;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

import static com.infiext.soybean.po.table.SysMenuQueryTableDef.SYS_MENU_QUERY;

@Service
public class SysMenuQueryServiceImpl implements SysMenuQueryService {
    @Resource
    private RelationResetService relationResetService;
    
    @Resource
    private SysMenuQueryMapper mapper;

    @Override
    public void resetMenuQuery(String menuId, List<SysMenuQueryPO> query) {
        if (menuId == null) return;
        relationResetService.resetRelations(
                menuId,
                query,
                new RelationResetHandler<>() {
                    @Override
                    public BaseMapper<SysMenuQueryPO> getMapper() {
                        return mapper;
                    }

                    @Override
                    public Supplier<SysMenuQueryPO> getEntitySupplier() {
                        return SysMenuQueryPO::create;
                    }

                    @Override
                    public QueryWrapper buildDeleteQuery(String id) {
                        return new QueryWrapper().where(SYS_MENU_QUERY.MENU_ID.eq(id));
                    }

                    @Override
                    public void setForeignKey(SysMenuQueryPO po, String id) {
                        po.setMenuId(id);
                    }
                }
        );
    }
}