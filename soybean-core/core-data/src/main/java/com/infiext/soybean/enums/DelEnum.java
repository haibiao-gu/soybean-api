package com.infiext.soybean.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DelEnum {
    DELETED(1, "已删除"),
    NORMAL(0, "正常");

    @EnumValue
    private final int code;
    private final String desc;
}
