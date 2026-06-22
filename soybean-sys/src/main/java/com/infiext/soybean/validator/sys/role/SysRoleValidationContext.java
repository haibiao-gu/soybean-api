package com.infiext.soybean.validator.sys.role;

import com.infiext.soybean.po.SysRolePO;
import com.infiext.soybean.validator.ValidationContext;
import com.infiext.soybean.validator.Validator;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SysRoleValidationContext extends ValidationContext<SysRolePO> {

    public SysRoleValidationContext(List<Validator<SysRolePO>> validators) {
        super(validators);
    }

    public void validateAll(SysRolePO po) {
        validate(po); // 执行所有验证
    }

}