package com.infiext.soybean.enums;

import com.infiext.soybean.utils.excel.annotation.ExcelEnum;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileStatusEnum {
    UPLOADING("0", "上传中"),
    COMPLETED("1", "完成"),
    FAILED("2", "失败");

    @EnumValue
    private final String code;
    @ExcelEnum
    private final String desc;
}
