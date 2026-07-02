package com.infiext.soybean.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigGroupEnum {
    MAIL("MAIL", "邮箱配置"),
    UPLOAD("UPLOAD", "上传配置");

    @EnumValue
    private final String code;
    private final String desc;
}
