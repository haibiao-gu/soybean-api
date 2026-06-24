package com.infiext.soybean.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SendStatusEnum {
    SENDING(1, "发送中"),
    SUCCESS(2, "成功"),
    FAILED(3, "失败");

    @EnumValue
    private final int code;
    private final String desc;
}
