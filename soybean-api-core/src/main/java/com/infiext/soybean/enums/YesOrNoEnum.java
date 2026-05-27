package com.infiext.soybean.enums;

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
    private final String desc;
}
