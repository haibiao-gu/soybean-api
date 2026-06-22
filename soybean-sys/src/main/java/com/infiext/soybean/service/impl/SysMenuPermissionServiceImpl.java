package com.infiext.soybean.service.impl;

import com.infiext.soybean.mapper.SysMenuPermissionMapper;
import com.infiext.soybean.po.SysMenuPermissionPO;
import com.infiext.soybean.service.SysMenuPermissionService;
import com.infiext.soybean.utils.RelationResetHandler;
import com.infiext.soybean.utils.RelationResetService;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

import static com.infiext.soybean.po.table.SysMenuPermissionTableDef.SYS_MENU_PERMISSION;

@Service
public class SysMenuPermissionServiceImpl implements SysMenuPermissionService {
    @Resource
    private RelationResetService relationResetService;

    @Resource
    private SysMenuPermissionMapper mapper;

    @Override
    public void resetMenuPermissions(String parentId, List<SysMenuPermissionPO> relations) {
        if (parentId == null) return;
        relationResetService.resetRelations(
                parentId,
                relations,
                new RelationResetHandler<>() {
                    @Override
                    public BaseMapper<SysMenuPermissionPO> getMapper() {
                        return mapper;
                    }

                    @Override
                    public Supplier<SysMenuPermissionPO> getEntitySupplier() {
                        return SysMenuPermissionPO::create;
                    }

                    @Override
                    public QueryWrapper buildDeleteQuery(String id) {
                        return new QueryWrapper().where(SYS_MENU_PERMISSION.MENU_ID.eq(id));
                    }

                    @Override
                    public void setForeignKey(SysMenuPermissionPO po, String id) {
                        po.setMenuId(id);
                    }
                }
        );
    }
    
}
