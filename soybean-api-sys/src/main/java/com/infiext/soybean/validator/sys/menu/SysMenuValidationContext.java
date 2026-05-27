package com.infiext.soybean.validator.sys.menu;

import com.infiext.soybean.po.SysMenuPO;
import com.infiext.soybean.validator.ValidationContext;
import com.infiext.soybean.validator.Validator;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SysMenuValidationContext extends ValidationContext<SysMenuPO> {

    public SysMenuValidationContext(List<Validator<SysMenuPO>> validators) {
        super(validators);
    }

    public void validateAll(SysMenuPO po) {
        validate(po); // 执行所有验证
    }

}