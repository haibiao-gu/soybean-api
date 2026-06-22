package com.infiext.soybean.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 枚举描述字段注解
 * <p>标注在枚举类的 desc 字段上，用于标识该字段为 Excel 显示值</p>
 * <p>value 字段会自动通过 @EnumValue 注解识别，无需额外标注</p>
 *
 * @author system
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelEnum {
}
