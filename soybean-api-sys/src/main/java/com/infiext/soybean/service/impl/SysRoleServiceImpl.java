package com.infiext.soybean.service.impl;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.mapper.SysRoleMapper;
import com.infiext.soybean.po.SysRoleMenuPO;
import com.infiext.soybean.po.SysRolePO;
import com.infiext.soybean.service.SysRoleMenuService;
import com.infiext.soybean.service.SysRoleService;
import com.infiext.soybean.utils.SortUtil;
import com.infiext.soybean.validator.sys.role.SysRoleValidationContext;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.infiext.soybean.po.table.SysRoleMenuTableDef.SYS_ROLE_MENU;
import static com.infiext.soybean.po.table.SysRoleTableDef.SYS_ROLE;
import static com.mybatisflex.core.query.QueryMethods.distinct;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleValidationContext validator;
    @Resource
    private SysRoleMapper mapper;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 创建
     */
    @Transactional
    @Override
    public SysRolePO create(SysRolePO po) {
        validator.validateAll(po);
        po.save();
        sysRoleMenuService.resetRoleMenu(po.getId(), po.getMenus());
        return po;
    }

    /**
     * 更新
     */
    @Transactional
    @Override
    public SysRolePO update(SysRolePO po) {
        validator.validateAll(po);
        boolean status = po.updateById();
        if (!status) {
            throw new BusinessException("修改失败，数据已被他人更新！");
        }
        sysRoleMenuService.resetRoleMenu(po.getId(), po.getMenus());
        return po;
    }

    /**
     * 逻辑删除
     */
    @Transactional
    @Override
    public void deleteByIds(List<String> ids) {
        mapper.deleteBatchByIds(ids);
        for (String id : ids) {
            sysRoleMenuService.resetRoleMenu(id, new ArrayList<>());
        }
    }

    /**
     * 获取
     */
    @Override
    public SysRolePO getById(String id) {
        return SysRolePO.create().setId(id).withRelations().oneById();
    }

    /**
     * 获取分页
     */
    @Override
    public Page<SysRolePO> getPage(SysRolePO query, Page<SysRolePO> page, SortParam sort) {
        QueryWrapper queryWrapper = getQueryWrapper(query, sort);
        return mapper.paginateWithRelations(page, queryWrapper);
    }

    /**
     * 获取列表
     */
    @Override
    public List<SysRolePO> getList(SysRolePO query, SortParam sort) {
        QueryWrapper queryWrapper = getQueryWrapper(query, sort);
        return mapper.selectListWithRelationsByQuery(queryWrapper);
    }

    /**
     * 获取查询条件
     */
    private QueryWrapper getQueryWrapper(SysRolePO query, SortParam sort) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(SYS_ROLE.DEFAULT_COLUMNS);

        queryWrapper.and(SYS_ROLE.ROLE_NAME.like(query.getRoleName()));
        queryWrapper.and(SYS_ROLE.ROLE_CODE.like(query.getRoleCode()));

        return SortUtil.orderBy(queryWrapper, sort, SysRolePO.class, SYS_ROLE.CREATE_TIME.asc());
    }

    /**
     * 获取角色菜单ID
     *
     * @param roleId 角色ID
     * @return 角色菜单ID
     */
    @Override
    public List<String> getRoleMenuIds(String roleId) {
        return SysRoleMenuPO.create()
                .select(distinct(SYS_ROLE_MENU.MENU_ID))
                .where(SYS_ROLE_MENU.ROLE_ID.eq(roleId))
                .listAs(String.class);
    }

    /**
     * 获取角色菜单ID
     *
     * @param roleIds 角色ID
     * @return 角色菜单ID
     */
    @Override
    public List<String> getRoleMenuIds(List<String> roleIds) {
        return SysRoleMenuPO.create()
                .select(distinct(SYS_ROLE_MENU.MENU_ID))
                .where(SYS_ROLE_MENU.ROLE_ID.in(roleIds))
                .listAs(String.class);
    }
}