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
            return null;
        }
        String normalized = value.trim();
        for (FileStoreType type : values()) {
            if (type.code.equalsIgnoreCase(normalized) || type.name().equalsIgnoreCase(normalized)) {
                return type;
            }
        }
        return null;
    }
}
