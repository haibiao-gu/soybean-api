package com.infiext.soybean.exception;

import lombok.Getter;

/**
 * 自定义业务异常（适用于业务逻辑错误，如参数错误、资源不存在等）
 * 包含自定义错误码，适配统一响应体格式
 */
@Getter // Lombok：自动生成getter方法（无需手动写）
public class BusinessException extends RuntimeException {
    /**
     * 自定义错误码（区别于通用500）
     */
    private final int code;

    /**
     * 构造器1：指定错误码和错误信息
     */
    public BusinessException(int code, String message) {
        super(message); // 父类保存错误信息
        this.code = code;
    }

    /**
     * 构造器2：快速创建通用业务异常（默认错误码400，适用于参数错误等）
     */
    public BusinessException(String message) {
        this(400, message);
    }

    /**
     * 构造器3：快速创建指定错误码的异常（简化调用）
     * 示例：new BusinessException(404, "用户不存在")
     */
    public static BusinessException of(int code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 常用快捷异常（静态方法，简化业务层调用）
     */
    // 参数错误（400）
    public static BusinessException paramError(String message) {
        return new BusinessException(400, message);
    }

    // 资源不存在（404）
    public static BusinessException notFound(String message) {
        return new BusinessException(404, message);
    }

    // 权限不足（403）
    public static BusinessException forbidden(String message) {
        return new BusinessException(403, message);
    }
}