package com.infiext.soybean.service.impl;

import com.infiext.soybean.mapper.SysRoleMenuMapper;
import com.infiext.soybean.po.SysRoleMenuPO;
import com.infiext.soybean.service.SysRoleMenuService;
import com.infiext.soybean.utils.RelationResetHandler;
import com.infiext.soybean.utils.RelationResetService;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

import static com.infiext.soybean.po.table.SysRoleMenuTableDef.SYS_ROLE_MENU;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService {
    @Resource
    private RelationResetService relationResetService;

    @Resource
    private SysRoleMenuMapper mapper;

    @Override
    public void resetRoleMenus(String parentId, List<SysRoleMenuPO> relations) {
        if (parentId == null) return;
        relationResetService.resetRelations(
                parentId,
                relations,
                new RelationResetHandler<>() {
                    @Override
                    public BaseMapper<SysRoleMenuPO> getMapper() {
                        return mapper;
                    }

                    @Override
                    public Supplier<SysRoleMenuPO> getEntitySupplier() {
                        return SysRoleMenuPO::create;
                    }

                    @Override
                    public QueryWrapper buildDeleteQuery(String id) {
                        return new QueryWrapper().where(SYS_ROLE_MENU.ROLE_ID.eq(id));
                    }

                    @Override
                    public void setForeignKey(SysRoleMenuPO po, String id) {
                        po.setRoleId(id);
                    }
                }
        );
    }

}
