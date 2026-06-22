package com.infiext.soybean.validator.sys.user;

import com.infiext.soybean.po.SysUserPO;
import com.infiext.soybean.validator.ValidationContext;
import com.infiext.soybean.validator.Validator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysUserValidationContext extends ValidationContext<SysUserPO> {

    public SysUserValidationContext(List<Validator<SysUserPO>> validators) {
        super(validators);
    }

    // 可以添加用户特有的验证方法
    public void validateAll(SysUserPO po) {
        validate(po); // 执行所有验证
    }

}
