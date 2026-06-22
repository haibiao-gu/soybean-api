package com.infiext.soybean.utils.excel.support;

import com.infiext.soybean.utils.excel.annotation.ExcelEnum;
import com.mybatisflex.annotation.EnumValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * Excel 枚举映射工具类
 * <p>从枚举类中自动提取映射关系：</p>
 * <ul>
 *   <li>value 字段：通过 @EnumValue 注解自动识别</li>
 *   <li>desc 字段：通过 @ExcelEnum 注解标识</li>
 * </ul>
 * <p>映射关系：Excel 中的 desc 值 ↔ Java 枚举常量</p>
 *
 * @author system
 * @since 1.0
 */
public class ExcelEnumUtils {
    private static final String LEGACY_EXCEL_ENUM_ANNOTATION =
            "com.infiext.soybean.enums.annotation.ExcelEnum";

    /**
     * 从枚举类中智能提取映射关系
     * <p>建立映射：枚举常量名（如 ENABLED）→ Excel 显示值（如 正常）</p>
     * <p>与手动配置的 kv 格式保持一致：key 为枚举常量名，value 为显示值</p>
     *
     * @param enumClass 枚举类的 Class 对象
     * @return 映射 Map，key 为枚举常量名，value 为 desc 值
     */
    public static LinkedHashMap<String, String> getEnumMap(Class<? extends Enum<?>> enumClass) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        if (enumClass == null) {
            return map;
        }

        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null || enumConstants.length == 0) {
            return map;
        }

        // 智能识别字段
        FieldInfo fieldInfo = identifyFields(enumClass);

        // 遍历枚举常量，建立映射：枚举常量名 → desc 值
        for (Enum<?> enumConstant : enumConstants) {
            try {
                String descValue = getFieldValue(enumConstant, fieldInfo.descField);
                String enumName = enumConstant.name();

                // 如果 desc 获取失败，使用枚举名称
                if (descValue == null || descValue.isEmpty()) {
                    descValue = enumName;
                }

                // 建立映射：枚举常量名 → Excel 显示值
                map.put(enumName, descValue);
            } catch (Exception e) {
                // 如果提取失败，使用枚举名称
                map.put(enumConstant.name(), enumConstant.name());
            }
        }

        return map;
    }

    /**
     * 智能识别枚举类中的 value 和 desc 字段
     *
     * @param enumClass 枚举类
     * @return 字段信息
     */
    private static FieldInfo identifyFields(Class<? extends Enum<?>> enumClass) {
        Field[] fields = enumClass.getDeclaredFields();

        Field valueField = null;
        Field descField = null;

        // 第一步：查找 @EnumValue 注解标记的字段（value）
        for (Field field : fields) {
            if (field.isAnnotationPresent(EnumValue.class)) {
                valueField = field;
                break;
            }
        }

        // 第二步：查找 @ExcelEnum 注解标记的字段（desc）
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelEnum.class) || hasLegacyExcelEnumAnnotation(field)) {
                descField = field;
                break;
            }
        }

        // 第三步：如果还没找到，按命名约定查找
        if (valueField == null) {
            valueField = findFieldByNamingConvention(fields,
                    new String[]{"code", "value", "id", "key"});
        }
        if (descField == null) {
            descField = findFieldByNamingConvention(fields,
                    new String[]{"desc", "name", "label", "text", "title"});
        }

        return new FieldInfo(
                valueField != null ? valueField.getName() : "code",
                descField != null ? descField.getName() : "desc"
        );
    }

    /**
     * 按命名约定查找字段
     *
     * @param fields         字段数组
     * @param candidateNames 候选字段名列表
     * @return 找到的字段，未找到返回 null
     */
    private static Field findFieldByNamingConvention(Field[] fields, String[] candidateNames) {
        for (String candidateName : candidateNames) {
            for (Field field : fields) {
                if (candidateName.equalsIgnoreCase(field.getName())) {
                    return field;
                }
            }
        }
        return null;
    }

    private static boolean hasLegacyExcelEnumAnnotation(Field field) {
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            if (LEGACY_EXCEL_ENUM_ANNOTATION.equals(annotation.annotationType().getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过反射获取枚举常量的字段值
     *
     * @param enumConstant 枚举常量
     * @param fieldName    字段名
     * @return 字段值，获取失败返回 null
     */
    private static String getFieldValue(Enum<?> enumConstant, String fieldName) {
        try {
            // 先尝试通过 getter 方法获取
            String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            Method getter = enumConstant.getClass().getMethod(getterName);
            Object value = getter.invoke(enumConstant);
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            // getter 方法不存在，尝试直接访问字段
            try {
                Field field = enumConstant.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(enumConstant);
                return value != null ? value.toString() : null;
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * 合并映射关系
     * <p>优先级：手动配置的 kv > enumClass 自动识别</p>
     *
     * @param manualKv  手动配置的 kv 字符串
     * @param enumClass 枚举类
     * @return 最终的映射 Map
     */
    public static LinkedHashMap<String, String> mergeKvMap(String manualKv, Class<? extends Enum<?>> enumClass) {
        // 如果有手动配置且不为空，优先使用手动配置
        if (manualKv != null && !manualKv.trim().isEmpty()) {
            return ExcelFieldExtractor.getKvMap(manualKv);
        }

        // 否则使用枚举映射
        if (enumClass != null) {
            return getEnumMap(enumClass);
        }

        return new LinkedHashMap<>();
    }

    /**
     * 字段信息内部类
     */
    private static class FieldInfo {
        String valueField;
        String descField;

        FieldInfo(String valueField, String descField) {
            this.valueField = valueField;
            this.descField = descField;
        }
    }
}
