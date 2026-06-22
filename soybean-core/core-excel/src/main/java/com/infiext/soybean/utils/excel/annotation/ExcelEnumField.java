package com.infiext.soybean.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 枚举字段映射注解
 * <p>标注在 PO 类的字段上，指定该字段使用的枚举类型</p>
 * <p>系统会自动从枚举类中提取映射关系（desc 值 ↔ 枚举常量名）</p>
 * <p>枚举类的 desc 字段需要使用 @ExcelEnum 注解标注</p>
 *
 * @author system
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelEnumField {

    /**
     * 枚举类型 Class
     * <p>指定该字段对应的枚举类</p>
     *
     * @return 枚举类型的 Class
     */
    Class<? extends Enum<?>> value();
}

