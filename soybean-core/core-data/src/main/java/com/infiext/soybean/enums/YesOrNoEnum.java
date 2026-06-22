package com.infiext.soybean.enums;

import com.infiext.soybean.utils.excel.annotation.ExcelEnum;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesOrNoEnum {
    Y(1, "是"),
    N(0, "否");

    @EnumValue
    private final int code;
    @ExcelEnum
    private final String desc;
}
