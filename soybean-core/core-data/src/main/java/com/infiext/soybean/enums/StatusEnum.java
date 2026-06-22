package com.infiext.soybean.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    DISABLED("0", "停用"),
    ENABLED("1", "正常");

    @EnumValue
    private final String code;
    private final String desc;
}
