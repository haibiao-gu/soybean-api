package com.infiext.soybean.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderEnum {
    ascend(true, "升序"),
    descend(false, "降序"),
    none(null, "无");

    private final Boolean code;
    private final String desc;
}