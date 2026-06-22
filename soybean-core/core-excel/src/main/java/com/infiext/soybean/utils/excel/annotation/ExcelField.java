package com.infiext.soybean.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 通用字段注解
 * <p>标注在 Java Bean 字段上，同时支持 Excel 导入和导出功能</p>
 * <p>使用此注解可以同时配置导入导出相关的属性，简化代码编写</p>
 *
 * @author system
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    /**
     * 表头名称（Excel 列名）
     * <p>用于导入时匹配 Excel 文件中的列标题，导出时作为列名显示</p>
     *
     * @return 列名
     */
    String value();

    /**
     * 导出排序权重
     * <p>数值越小越靠前，默认按 Java 类字段声明顺序导出</p>
     * <p>仅在导出时生效</p>
     *
     * @return 排序权重值，默认为 0
     */
    int sort() default 0;

    /**
     * 枚举值映射配置
     * <p>格式：代码值-显示值;代码值-显示值，例如：0-未知;1-男;2-女</p>
     * <p>导入时会将 Excel 中的显示值转换为对应的代码值</p>
     * <p>导出时会将字段的代码值转换为对应的显示值</p>
     *
     * @return 映射配置字符串，默认为空表示不进行映射转换
     */
    String kv() default "";

    /**
     * 是否必填字段
     * <p>设置为 true 时，导入数据中该字段不能为空，否则会产生验证错误</p>
     * <p>仅在导入时生效</p>
     *
     * @return true 表示必填，false 表示非必填，默认为 false
     */
    boolean required() default false;

    /**
     * 字段最大长度限制
     * <p>导入时会校验字段值的长度，超过此长度会产生验证错误</p>
     * <p>仅在导入时生效</p>
     *
     * @return 最大字符长度，默认为 255
     */
    int maxLength() default 255;

    /**
     * 唯一性验证标识
     * <p>设置为 true 时，该字段会参与唯一性校验</p>
     * <p>多个字段都设置 unique=true 时，会进行联合唯一性验证</p>
     * <p>仅在导入时生效</p>
     *
     * @return true 表示需要唯一性验证，false 表示不需要，默认为 false
     */
    boolean unique() default false;

    /**
     * 导出模板示例值
     * <p>当生成 Excel 导入模板时，若配置了此值则直接使用该值作为示例</p>
     * <p>优先级高于 kv 映射，配置后不会进行枚举映射转换</p>
     * <p>仅在导出时生效</p>
     *
     * @return 示例值字符串，默认为空表示不显示示例
     */
    String example() default "";

}
