package com.infiext.soybean.validator.sys.user;


import cn.hutool.core.text.CharSequenceUtil;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.mapper.SysUserMapper;
import com.infiext.soybean.po.SysUserPO;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import static com.infiext.soybean.po.table.SysUserTableDef.SYS_USER;

@Component
public class SysUserPhoneValidator implements SysUserValidator {
    @Resource
    private SysUserMapper mapper;

    @Override
    public void validate(SysUserPO po) {
        if (CharSequenceUtil.isBlank(po.getUserPhone())) return;

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(SYS_USER.USER_PHONE.eq(po.getUserPhone()));

        if (po.getId() != null) {
            queryWrapper.and(SYS_USER.ID.ne(po.getId()));
        }

        long count = mapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException("手机号已存在");
        }
    }

    @Override
    public String getFieldName() {
        return "phone";
    }
}
