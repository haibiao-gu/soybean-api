package com.infiext.soybean.utils.excel.support;

import com.infiext.soybean.utils.excel.annotation.ExcelEnumField;
import com.infiext.soybean.utils.excel.annotation.ExcelExport;
import com.infiext.soybean.utils.excel.annotation.ExcelField;
import com.infiext.soybean.utils.excel.model.ExcelClassField;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Excel 字段提取器
 * <p>负责从 Java Bean 类中提取带有 Excel 注解的字段信息，并构建字段元数据列表</p>
 *
 * @author system
 * @since 1.0
 */
public class ExcelFieldExtractor {

    /**
     * 获取类中所有 Excel 导出字段的元数据列表
     * <p>如果类中存在 @ExcelExport 注解字段，则只返回带注解的字段；否则返回所有字段</p>
     *
     * @param clazz 目标 Java Bean 类型
     * @param <T>   泛型类型参数
     * @return 按排序权重排列的 Excel 字段元数据列表
     */
    public static <T> List<ExcelClassField> getExcelClassFieldList(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        boolean hasExportAnnotation = false;
        Map<Integer, List<ExcelClassField>> map = new LinkedHashMap<>();
        List<Integer> sortList = new ArrayList<>();
        for (Field field : fields) {
            ExcelClassField cf = getExcelClassField(field);
            if (cf.isHasAnnotation()) {
                hasExportAnnotation = true;
            }
            int sort = cf.getSort();
            if (map.containsKey(sort)) {
                map.get(sort).add(cf);
            } else {
                List<ExcelClassField> list = new ArrayList<>();
                list.add(cf);
                sortList.add(sort);
                map.put(sort, list);
            }
        }
        Collections.sort(sortList);

        // 根据是否存在注解决定返回哪些字段
        List<ExcelClassField> headFieldList = new ArrayList<>();
        if (hasExportAnnotation) {
            for (Integer sort : sortList) {
                for (ExcelClassField cf : map.get(sort)) {
                    if (cf.isHasAnnotation()) {
                        headFieldList.add(cf);
                    }
                }
            }
        } else {
            headFieldList.addAll(map.get(0));
        }
        return headFieldList;
    }

    /**
     * 从单个 Field 对象提取 Excel 字段元数据
     * <p>解析 @ExcelExport、@ExcelImport 或 @ExcelField 注解配置，构建 ExcelClassField 对象</p>
     * <p>优先使用 @ExcelField 通用注解，如果不存在则分别检查 @ExcelExport 和 @ExcelImport</p>
     *
     * @param field Java 反射字段对象
     * @return 包含字段元数据的 ExcelClassField 对象
     */
    public static ExcelClassField getExcelClassField(Field field) {
        ExcelClassField cf = new ExcelClassField();
        String fieldName = field.getName();
        cf.setFieldName(fieldName);

        // 优先检查 @ExcelField 通用注解
        ExcelField excelField = field.getAnnotation(ExcelField.class);
        if (excelField != null) {
            cf.setHasAnnotation(true);
            cf.setName(excelField.value());
            String example = getString(excelField.example());
            if (!example.isEmpty()) {
                if (isNumeric(example) && example.length() < 8) {
                    cf.setExample(Double.valueOf(example));
                } else {
                    cf.setExample(example);
                }
            } else {
                cf.setExample("");
            }
            cf.setSort(excelField.sort());

            // 检查是否有 @ExcelEnumField 注解
            ExcelEnumField enumField = field.getAnnotation(ExcelEnumField.class);
            LinkedHashMap<String, String> kvMap;
            if (enumField != null) {
                kvMap = ExcelEnumUtils.getEnumMap(enumField.value());
            } else if (field.getType().isEnum()) {
                // 自动识别枚举类型
                @SuppressWarnings("unchecked")
                Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) field.getType();
                kvMap = ExcelEnumUtils.getEnumMap(enumClass);
            } else {
                kvMap = ExcelFieldExtractor.getKvMap(excelField.kv());
            }
            cf.setKvMap(kvMap);
            return cf;
        }

        // 检查 @ExcelExport 注解
        ExcelExport annotation = field.getAnnotation(ExcelExport.class);
        if (annotation == null) {
            cf.setHasAnnotation(false);
            cf.setName(fieldName);
            cf.setSort(0);
            return cf;
        }
        cf.setHasAnnotation(true);
        cf.setName(annotation.value());
        String example = getString(annotation.example());
        if (!example.isEmpty()) {
            if (isNumeric(example) && example.length() < 8) {
                cf.setExample(Double.valueOf(example));
            } else {
                cf.setExample(example);
            }
        } else {
            cf.setExample("");
        }
        cf.setSort(annotation.sort());

        // 检查是否有 @ExcelEnumField 注解
        ExcelEnumField enumField = field.getAnnotation(ExcelEnumField.class);
        LinkedHashMap<String, String> kvMap;
        if (enumField != null) {
            kvMap = ExcelEnumUtils.getEnumMap(enumField.value());
        } else if (field.getType().isEnum()) {
            // 自动识别枚举类型
            @SuppressWarnings("unchecked")
            Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) field.getType();
            kvMap = ExcelEnumUtils.getEnumMap(enumClass);
        } else {
            kvMap = ExcelFieldExtractor.getKvMap(annotation.kv());
        }
        cf.setKvMap(kvMap);
        return cf;
    }

    /**
     * 解析枚举映射字符串为 LinkedHashMap
     * <p>格式：key1-value1;key2-value2，例如：0-未知;1-男;2-女</p>
     *
     * @param kv 映射配置字符串
     * @return 解析后的 LinkedHashMap，key 为代码值，value 为显示值
     */
    public static LinkedHashMap<String, String> getKvMap(String kv) {
        LinkedHashMap<String, String> kvMap = new LinkedHashMap<>();
        if (kv.isEmpty()) {
            return kvMap;
        }
        String[] kvs = kv.split(";");
        for (String each : kvs) {
            String[] eachKv = getString(each).split("-");
            if (eachKv.length != 2) {
                continue;
            }
            String k = eachKv[0];
            String v = eachKv[1];
            if (k.isEmpty() || v.isEmpty()) {
                continue;
            }
            kvMap.put(k, v);
        }
        return kvMap;
    }

    /**
     * 判断字符串是否为纯数字
     *
     * @param str 待检查的字符串
     * @return true 表示是数字，false 表示不是数字
     */
    private static boolean isNumeric(String str) {
        if (Objects.nonNull(str) && "0.0".equals(str)) {
            return true;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 安全获取字符串，去除首尾空白
     *
     * @param s 原始字符串
     * @return 处理后的字符串，null 返回空字符串
     */
    private static String getString(String s) {
        if (s == null) {
            return "";
        }
        if (s.isEmpty()) {
            return s;
        }
        return s.trim();
    }
}
