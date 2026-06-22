package com.infiext.soybean.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 导出注解
 * <p>标注在 Java Bean 字段上，用于控制 Excel 导出时的列名、排序、枚举映射和示例值</p>
 *
 * @author system
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelExport {

    /**
     * 表头名称（Excel 列名）
     *
     * @return 导出时显示的列标题
     */
    String value();

    /**
     * 导出排序权重
     * <p>数值越小越靠前，默认按 Java 类字段声明顺序导出</p>
     *
     * @return 排序权重值，默认为 0
     */
    int sort() default 0;

    /**
     * 枚举值映射配置
     * <p>格式：代码值-显示值;代码值-显示值，例如：0-未知;1-男;2-女</p>
     * <p>导出时会将字段的代码值转换为对应的显示值</p>
     *
     * @return 映射配置字符串，默认为空表示不进行映射转换
     */
    String kv() default "";

    /**
     * 导出模板示例值
     * <p>当生成 Excel 导入模板时，若配置了此值则直接使用该值作为示例</p>
     * <p>优先级高于 kv 映射，配置后不会进行枚举映射转换</p>
     *
     * @return 示例值字符串，默认为空表示不显示示例
     */
    String example() default "";

}

