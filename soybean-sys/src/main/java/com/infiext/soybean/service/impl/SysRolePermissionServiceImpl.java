package com.infiext.soybean.service.impl;

import com.infiext.soybean.mapper.SysRolePermissionMapper;
import com.infiext.soybean.po.SysRolePermissionPO;
import com.infiext.soybean.service.SysRolePermissionService;
import com.infiext.soybean.utils.RelationResetHandler;
import com.infiext.soybean.utils.RelationResetService;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

import static com.infiext.soybean.po.table.SysRolePermissionTableDef.SYS_ROLE_PERMISSION;

@Service
public class SysRolePermissionServiceImpl implements SysRolePermissionService {
    @Resource
    private RelationResetService relationResetService;

    @Resource
    private SysRolePermissionMapper mapper;

    @Override
    public void resetRolePermissions(String parentId, List<SysRolePermissionPO> relations) {
        if (parentId == null) return;
        relationResetService.resetRelations(
                parentId,
                relations,
                new RelationResetHandler<>() {
                    @Override
                    public BaseMapper<SysRolePermissionPO> getMapper() {
                        return mapper;
                    }

                    @Override
                    public Supplier<SysRolePermissionPO> getEntitySupplier() {
                        return SysRolePermissionPO::create;
                    }

                    @Override
                    public QueryWrapper buildDeleteQuery(String id) {
                        return new QueryWrapper().where(SYS_ROLE_PERMISSION.ROLE_ID.eq(id));
                    }

                    @Override
                    public void setForeignKey(SysRolePermissionPO po, String id) {
                        po.setRoleId(id);
                    }
                }
        );
    }
    
}
