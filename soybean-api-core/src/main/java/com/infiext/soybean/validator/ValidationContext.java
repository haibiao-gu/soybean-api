package com.infiext.soybean.validator;

import java.util.List;
import java.util.Set;

/**
 * 通用验证上下文
 *
 * @param <T> 验证实体类型
 */
public class ValidationContext<T> {

    private final List<Validator<T>> validators;

    public ValidationContext(List<Validator<T>> validators) {
        this.validators = validators;
    }

    /**
     * 验证实体
     *
     * @param entity 待验证实体
     * @param fields 指定验证的字段，如果不指定则验证所有字段
     */
    public void validate(T entity, String... fields) {
        List<Validator<T>> targetValidators = validators;

        if (fields.length > 0) {
            Set<String> fieldSet = Set.of(fields);
            targetValidators = validators.stream()
                    .filter(v -> fieldSet.contains(v.getFieldName()))
                    .toList();
        }

        for (Validator<T> validator : targetValidators) {
            validator.validate(entity);
        }
    }
}