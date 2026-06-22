package com.infiext.soybean.utils.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 导入注解
 * <p>标注在 Java Bean 字段上，用于控制 Excel 导入时的字段映射、数据验证和唯一性检查</p>
 *
 * @author system
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelImport {

    /**
     * Excel 列名（表头名称）
     * <p>用于匹配 Excel 文件中的列标题</p>
     *
     * @return 匹配的列名
     */
    String value();

    /**
     * 枚举值映射配置
     * <p>格式：代码值-显示值;代码值-显示值，例如：0-未知;1-男;2-女</p>
     * <p>导入时会将 Excel 中的显示值转换为对应的代码值存储到字段中</p>
     *
     * @return 映射配置字符串，默认为空表示不进行映射转换
     */
    String kv() default "";

    /**
     * 是否必填字段
     * <p>设置为 true 时，导入数据中该字段不能为空，否则会产生验证错误</p>
     *
     * @return true 表示必填，false 表示非必填，默认为 false
     */
    boolean required() default false;

    /**
     * 字段最大长度限制
     * <p>导入时会校验字段值的长度，超过此长度会产生验证错误</p>
     *
     * @return 最大字符长度，默认为 255
     */
    int maxLength() default 255;

    /**
     * 唯一性验证标识
     * <p>设置为 true 时，该字段会参与唯一性校验</p>
     * <p>多个字段都设置 unique=true 时，会进行联合唯一性验证</p>
     *
     * @return true 表示需要唯一性验证，false 表示不需要，默认为 false
     */
    boolean unique() default false;

}

