package com.infiext.soybean.service.impl;

import com.infiext.soybean.mapper.SysUserRoleMapper;
import com.infiext.soybean.po.SysUserRolePO;
import com.infiext.soybean.service.SysUserRoleService;
import com.infiext.soybean.utils.RelationResetHandler;
import com.infiext.soybean.utils.RelationResetService;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

import static com.infiext.soybean.po.table.SysUserRoleTableDef.SYS_USER_ROLE;

@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {
    @Resource
    private RelationResetService relationResetService;

    @Resource
    private SysUserRoleMapper mapper;

    @Override
    public void resetUserRole(String parentId, List<SysUserRolePO> relations) {
        if (parentId == null) return;
        relationResetService.resetRelations(
                parentId,
                relations,
                new RelationResetHandler<>() {
                    @Override
                    public BaseMapper<SysUserRolePO> getMapper() {
                        return mapper;
                    }

                    @Override
                    public Supplier<SysUserRolePO> getEntitySupplier() {
                        return SysUserRolePO::create;
                    }

                    @Override
                    public QueryWrapper buildDeleteQuery(String id) {
                        return new QueryWrapper().where(SYS_USER_ROLE.USER_ID.eq(id));
                    }

                    @Override
                    public void setForeignKey(SysUserRolePO po, String id) {
                        po.setUserId(id);
                    }
                }
        );
    }
}
