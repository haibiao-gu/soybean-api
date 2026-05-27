package com.infiext.soybean.domain;

import lombok.Data;

/**
 * 统一响应体
 * 格式：{code:int, data:Object, msg:string}
 */
@Data
public class Result<T> {
    /**
     * 响应码：200成功，500失败，其他自定义
     */
    private int code;

    /**
     * 响应数据：任意对象（成功时返回业务数据，失败时可返回null）
     */
    private T data;

    /**
     * 响应信息：成功/失败的提示文本
     */
    private String msg;

    // 私有构造，避免直接new
    private Result() {
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setData(data);
        result.setMsg("操作成功");
        return result;
    }

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return success(null);
    }

    // 失败响应（自定义错误码和提示）
    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setData(null);
        result.setMsg(msg);
        return result;
    }

    // 通用失败响应（默认500）
    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }
}