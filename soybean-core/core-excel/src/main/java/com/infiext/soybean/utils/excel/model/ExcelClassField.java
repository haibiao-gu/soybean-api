package com.infiext.soybean.utils.excel.model;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * Excel 字段元数据类
 * <p>用于存储 Excel 导入导出时字段的配置信息，包括字段名称、表头名称、映射关系等</p>
 *
 * @author system
 * @since 1.0
 */
@Data
public class ExcelClassField {

    /**
     * 字段名称（Java 类中的字段名）
     */
    private String fieldName;

    /**
     * 表头名称（Excel 中显示的列名）
     */
    private String name;

    /**
     * 映射关系
     * <p>用于枚举值的转换，key 为代码值，value 为显示值</p>
     * <p>例如：{"0": "未知", "1": "男", "2": "女"}</p>
     */
    private LinkedHashMap<String, String> kvMap = new LinkedHashMap<>();

    /**
     * 示例值
     * <p>用于生成 Excel 模板时的示例数据</p>
     */
    private Object example;

    /**
     * 排序权重
     * <p>数值越小越靠前，用于控制 Excel 列的显示顺序</p>
     */
    private int sort;

    /**
     * 是否为注解字段标识
     * <p>true: 该字段使用了 @ExcelExport 或 @ExcelImport 注解</p>
     * <p>false: 该字段未使用相关注解</p>
     */
    private boolean hasAnnotation;

}

