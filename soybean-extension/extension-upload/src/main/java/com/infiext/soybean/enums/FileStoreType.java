package com.infiext.soybean.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileStoreType {
    LOCAL("1", "本地"),
    MINIO("2", "MinIO");

    @EnumValue
    private final String code;
    private final String desc;

    public static FileStoreType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("文件存储类型不能为空或空");
        }
        String normalized = value.trim();
        for (FileStoreType type : values()) {
            if (type.code.equalsIgnoreCase(normalized) || type.name().equalsIgnoreCase(normalized)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的文件存储类型: " + value);
    }
}
