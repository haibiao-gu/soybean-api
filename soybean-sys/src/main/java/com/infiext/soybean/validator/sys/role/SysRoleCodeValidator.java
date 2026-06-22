package com.infiext.soybean.validator.sys.role;

import cn.hutool.core.text.CharSequenceUtil;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.mapper.SysRoleMapper;
import com.infiext.soybean.po.SysRolePO;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;

import static com.infiext.soybean.po.table.SysRoleTableDef.SYS_ROLE;

public class SysRoleCodeValidator implements SysRoleValidator{
    @Resource
    private SysRoleMapper mapper;

    @Override
    public void validate(SysRolePO po) {
        if (CharSequenceUtil.isBlank(po.getRoleCode())) return;

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(SYS_ROLE.ROLE_CODE.eq(po.getRoleCode()));

        if (po.getId() != null) {
            queryWrapper.and(SYS_ROLE.ID.ne(po.getId()));
        }

        long count = mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException("角色编号已存在");
        }
    }

    @Override
    public String getFieldName() {
        return "code";
    }
}
