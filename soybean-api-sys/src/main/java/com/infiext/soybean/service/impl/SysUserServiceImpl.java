package com.infiext.soybean.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.enums.StatusEnum;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.mapper.SysUserMapper;
import com.infiext.soybean.po.SysRolePO;
import com.infiext.soybean.po.SysUserPO;
import com.infiext.soybean.po.SysUserRolePO;
import com.infiext.soybean.service.SysUserRoleService;
import com.infiext.soybean.service.SysUserService;
import com.infiext.soybean.utils.SortUtil;
import com.infiext.soybean.validator.sys.user.SysUserValidationContext;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.infiext.soybean.po.table.SysRoleTableDef.SYS_ROLE;
import static com.infiext.soybean.po.table.SysUserRoleTableDef.SYS_USER_ROLE;
import static com.infiext.soybean.po.table.SysUserTableDef.SYS_USER;
import static com.mybatisflex.core.query.QueryMethods.distinct;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserValidationContext validator;
    @Resource
    private SysUserMapper mapper;

    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 创建用户
     */
    @Override
    @Transactional
    public SysUserPO createSysUser(SysUserPO po) {
        po.setPassword(MD5.create().digestHex16("123456"));
        validator.validateAll(po);
        po.save();
        sysUserRoleService.resetUserRole(po.getId(), po.getRoles());
        return po;
    }

    /**
     * 更新用户
     */
    @Override
    @Transactional
    public SysUserPO updateSysUser(SysUserPO po) {
        validator.validateAll(po);
        boolean status = po.updateById();
        if (!status) {
            throw new BusinessException("修改失败，数据已被他人更新！");
        }
        sysUserRoleService.resetUserRole(po.getId(), po.getRoles());
        return po;
    }

    /**
     * 逻辑删除用户
     */
    @Override
    @Transactional
    public void deleteSysUser(List<String> ids) {
        mapper.deleteBatchByIds(ids);
        for (String id : ids) {
            sysUserRoleService.resetUserRole(id, new ArrayList<>());
        }
    }

    /**
     * 获取用户
     */
    @Override
    public SysUserPO getSysUserById(String id) {
        return SysUserPO.create().setId(id).withRelations().oneById();
    }

    /**
     * 获取用户分页
     */
    @Override
    public Page<SysUserPO> getSysUserPage(SysUserPO query, Page<SysUserPO> page, SortParam sort) {
        QueryWrapper queryWrapper = getQueryWrapper(query, sort);
        return mapper.paginateWithRelations(page, queryWrapper);
    }

    /**
     * 获取用户列表
     */
    @Override
    public List<SysUserPO> getSysUserList(SysUserPO query, SortParam sort) {
        QueryWrapper queryWrapper = getQueryWrapper(query, sort);
        return mapper.selectListWithRelationsByQuery(queryWrapper);
    }

    /**
     * 获取查询条件
     */
    private QueryWrapper getQueryWrapper(SysUserPO query, SortParam sort) {
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.select(SYS_USER.DEFAULT_COLUMNS);

        queryWrapper.and(SYS_USER.USER_NAME.like(query.getUserName()));
        queryWrapper.and(SYS_USER.USER_PHONE.like(query.getUserPhone()));
        queryWrapper.and(SYS_USER.STATUS.eq(query.getStatus()));

        return SortUtil.orderBy(queryWrapper, sort, SysUserPO.class, SYS_USER.CREATE_TIME.asc());
    }

    /**
     * 获取用户 ID
     *
     * @param phone    手机号码
     * @param password 密码
     * @return 用户 ID
     */
    @Override
    public String getUserId(String phone, String password) {
        return SysUserPO.create()
                .select(SYS_USER.ID)
                .where(SYS_USER.USER_PHONE.eq(phone))
                .and(SYS_USER.PASSWORD.eq(MD5.create().digestHex16(password)))
                .and(SYS_USER.STATUS.eq(StatusEnum.ENABLED))
                .oneAs(String.class);
    }

    /**
     * 获取用户角色ID
     *
     * @param userId 用户ID
     * @return 角色ID
     */
    @Override
    public List<String> getUserRoleIds(String userId) {
        return SysUserRolePO.create()
                .select(distinct(SYS_USER_ROLE.ROLE_ID))
                .where(SYS_USER_ROLE.USER_ID.eq(userId))
                .listAs(String.class);
    }

    /**
     * 获取角色列表
     *
     * @param userId 用户 ID
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(String userId) {
        return SysRolePO.create()
                .select(SYS_ROLE.ROLE_CODE)
                .leftJoin(SYS_USER_ROLE).on(SYS_USER_ROLE.ROLE_ID.eq(SYS_ROLE.ID))
                .where(SYS_USER_ROLE.USER_ID.eq(userId))
                .listAs(String.class);
    }

    /**
     * 获取权限列表
     *
     * @param userId 用户 ID
     * @return 权限列表
     */
    @Override
    public List<String> getPermissionList(String userId) {
        return new ArrayList<>();
    }

    /**
     * 修改密码
     *
     * @param userId   用户ID
     * @param password 密码
     */
    @Override
    public void updatePassword(String userId, String password) {
        SysUserPO userPO = SysUserPO.create().setId(userId).oneById();
        userPO.setPassword(MD5.create().digestHex16(password));
        userPO.updateById();
    }
}
