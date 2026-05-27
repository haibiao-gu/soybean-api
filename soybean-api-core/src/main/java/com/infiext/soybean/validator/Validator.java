package com.infiext.soybean.validator;

/**
 * 通用验证器接口
 *
 * @param <T> 验证实体类型
 */
public interface Validator<T> {
    /**
     * 执行验证
     *
     * @param po 待验证实体
     */
    void validate(T po);

    /**
     * 获取字段名
     *
     * @return 字段名
     */
    String getFieldName();
}