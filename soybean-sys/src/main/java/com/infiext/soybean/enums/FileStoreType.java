package com.infiext.soybean.enums;

import com.infiext.soybean.utils.excel.annotation.ExcelEnum;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileStoreType {
    LOCAL("1", "本地");

    @EnumValue
    private final String code;
    @ExcelEnum
    private final String desc;
}
