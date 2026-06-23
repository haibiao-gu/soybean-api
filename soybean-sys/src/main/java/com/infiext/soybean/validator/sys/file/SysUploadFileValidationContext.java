package com.infiext.soybean.validator.sys.file;

import com.infiext.soybean.po.SysUploadFilePO;
import com.infiext.soybean.validator.ValidationContext;
import com.infiext.soybean.validator.Validator;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SysUploadFileValidationContext extends ValidationContext<SysUploadFilePO> {

    public SysUploadFileValidationContext(List<Validator<SysUploadFilePO>> validators) {
        super(validators);
    }

    public void validateAll(SysUploadFilePO po) {
        validate(po); // 执行所有验证
    }

}