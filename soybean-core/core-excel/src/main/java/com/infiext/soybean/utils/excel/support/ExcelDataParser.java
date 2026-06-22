package com.infiext.soybean.utils.excel.support;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.infiext.soybean.utils.excel.annotation.ExcelEnumField;
import com.infiext.soybean.utils.excel.annotation.ExcelField;
import com.infiext.soybean.utils.excel.annotation.ExcelImport;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel 数据解析器
 * <p>负责将 Excel 读取的 JSON 数据转换为 Java Bean 对象，支持数据验证和错误处理</p>
 *
 * @author system
 * @since 1.0
 */
public class ExcelDataParser {

    private static final String ROW_NUM = "rowNum";
    private static final String ROW_TIPS = "rowTips";
    private static final String ROW_DATA = "rowData";

    /**
     * 将 JSONArray 转换为 Java Bean 列表
     *
     * @param array Excel 读取的 JSON 数组数据
     * @param clazz 目标 Java Bean 类型
     * @param <T>   泛型类型参数
     * @return 转换后的 Java Bean 列表
     * @throws Exception 反射创建实例或字段赋值时可能抛出的异常
     */
    public static <T> List<T> getBeanList(JSONArray array, Class<T> clazz) throws Exception {
        List<T> list = new ArrayList<>();
        Map<Integer, String> uniqueMap = new HashMap<>(16);
        for (int i = 0; i < array.size(); i++) {
            list.add(getBean(clazz, array.getJSONObject(i), uniqueMap, i + 1));
        }
        return list;
    }
    
    /**
     * 将单个 JSONObject 转换为 Java Bean 对象
     * <p>处理特殊字段（rowNum、rowTips、rowData），执行数据验证和唯一性检查</p>
     *
     * @param c         目标 Java Bean 类型
     * @param obj       Excel 行数据对应的 JSON 对象
     * @param uniqueMap 用于唯一性校验的映射表，key 为行号，value 为唯一键值组合
     * @param rowNum    自然行号（从1开始）
     * @param <T>       泛型类型参数
     * @return 转换后的 Java Bean 对象，若存在验证错误则设置 rowTips 字段
     * @throws Exception 反射创建实例或字段赋值时可能抛出的异常
     */
    private static <T> T getBean(Class<T> c, JSONObject obj, Map<Integer, String> uniqueMap, int rowNum) throws Exception {
        var constructor = c.getDeclaredConstructor();
        constructor.setAccessible(true);
        T t = constructor.newInstance();
        Field[] fields = c.getDeclaredFields();
        List<String> errMsgList = new ArrayList<>();
        boolean hasRowTipsField = false;
        StringBuilder uniqueBuilder = new StringBuilder();
        for (Field field : fields) {
            switch (field.getName()) {
                case ROW_NUM -> {
                    field.setAccessible(true);
                    field.set(t, rowNum);
                    continue;
                }
                case ROW_TIPS -> {
                    hasRowTipsField = true;
                    continue;
                }
                case ROW_DATA -> {
                    field.setAccessible(true);
                    field.set(t, obj.toString());
                    continue;
                }
            }
            setFieldValue(t, field, obj, uniqueBuilder, errMsgList);
        }

        // 数据唯一性校验
        if (!uniqueBuilder.isEmpty()) {
            if (uniqueMap.containsValue(uniqueBuilder.toString())) {
                Set<Integer> rowNumKeys = uniqueMap.keySet();
                for (Integer num : rowNumKeys) {
                    if (uniqueMap.get(num).contentEquals(uniqueBuilder)) {
                        errMsgList.add(String.format("数据唯一性校验失败,(%s)与第%s行重复)", uniqueBuilder, num));
                    }
                }
            } else {
                uniqueMap.put(rowNum, uniqueBuilder.toString());
            }
        }

        // 若有错误信息，直接抛出异常
        if (!errMsgList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int size = errMsgList.size();
            for (int i = 0; i < size; i++) {
                if (i == size - 1) {
                    sb.append(errMsgList.get(i));
                } else {
                    sb.append(errMsgList.get(i)).append(";");
                }
            }
            throw new com.infiext.soybean.exception.BusinessException("第" + rowNum + "行数据验证失败: " + sb.toString());
        }

        // 若无错误且无 rowTips 字段，直接返回；否则组装错误信息并设置到 rowTips 字段
        if (errMsgList.isEmpty() && !hasRowTipsField) {
            return t;
        }
        StringBuilder sb = new StringBuilder();
        int size = errMsgList.size();
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                sb.append(errMsgList.get(i));
            } else {
                sb.append(errMsgList.get(i)).append(";");
            }
        }
        for (Field field : fields) {
            if (field.getName().equals(ROW_TIPS)) {
                field.setAccessible(true);
                field.set(t, sb.toString());
            }
        }
        return t;
    }

    /**
     * 根据注解配置设置字段值
     * <p>处理必填校验、唯一性标识、长度校验、枚举映射和类型转换</p>
     * <p>支持 @ExcelImport 和 @ExcelField 两种注解</p>
     *
     * @param t             目标 Java Bean 对象
     * @param field         当前处理的字段
     * @param obj           Excel 行数据对应的 JSON 对象
     * @param uniqueBuilder 用于构建唯一性校验键的字符串构建器
     * @param errMsgList    收集验证错误信息的列表
     */
    private static <T> void setFieldValue(T t, Field field, JSONObject obj, StringBuilder uniqueBuilder, List<String> errMsgList) {
        // 优先尝试获取 @ExcelField 注解
        ExcelField excelField = field.getAnnotation(ExcelField.class);
        ExcelImport annotation = null;

        if (excelField != null) {
            // 将 @ExcelField 适配为类似 @ExcelImport 的使用方式
            annotation = createExcelImportAdapter(excelField);
        } else {
            // 尝试获取 @ExcelImport 注解
            annotation = field.getAnnotation(ExcelImport.class);
        }

        if (annotation == null) {
            return;
        }
        String cname = annotation.value();
        if (cname.trim().isEmpty()) {
            return;
        }
        String val = null;
        if (obj.containsKey(cname)) {
            val = getString(obj.getStr(cname));
        }
        if (val == null) {
            return;
        }
        field.setAccessible(true);

        // 必填校验
        boolean require = annotation.required();
        if (require && val.isEmpty()) {
            errMsgList.add(String.format("[%s]不能为空", cname));
            return;
        }

        // 唯一性标识收集
        boolean unique = annotation.unique();
        if (unique) {
            if (!uniqueBuilder.isEmpty()) {
                uniqueBuilder.append("--").append(val);
            } else {
                uniqueBuilder.append(val);
            }
        }

        // 最大长度校验
        int maxLength = annotation.maxLength();
        if (maxLength > 0 && val.length() > maxLength) {
            errMsgList.add(String.format("[%s]长度不能超过%s个字符(当前%s个字符)", cname, maxLength, val.length()));
        }

        // 检查是否有 @ExcelEnumField 注解
        ExcelEnumField enumField = field.getAnnotation(ExcelEnumField.class);
        Class<? extends Enum<?>> enumClass = null;
        if (enumField != null) {
            enumClass = enumField.value();
        } else if (field.getType().isEnum()) {
            // 如果没有 @ExcelEnumField 注解但字段类型是枚举，自动使用该枚举类
            @SuppressWarnings("unchecked")
            Class<? extends Enum<?>> fieldType = (Class<? extends Enum<?>>) field.getType();
            enumClass = fieldType;
        }

        // 枚举值映射转换
        LinkedHashMap<String, String> kvMap = ExcelFieldExtractor.getKvMap(annotation.kv());

        // 合并映射关系：@ExcelEnumField > kv 配置
        if (enumClass != null) {
            kvMap = ExcelEnumUtils.getEnumMap(enumClass);
        }

        if (!kvMap.isEmpty()) {
            boolean isMatch = false;
            for (Map.Entry<String, String> entry : kvMap.entrySet()) {
                // key 是枚举常量名（如"ENABLED"），value 是 Excel 显示值（如"正常"）
                if (entry.getValue().equals(val)) {
                    val = entry.getKey();  // 转换为枚举常量名
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                errMsgList.add(String.format("[%s]的值不正确(当前值为%s)", cname, val));
                return;
            }
        }

        // 根据字段类型进行值转换
        String fieldClassName = field.getType().getSimpleName();
        try {
            if ("String".equalsIgnoreCase(fieldClassName)) {
                field.set(t, val);
            } else if ("boolean".equalsIgnoreCase(fieldClassName)) {
                field.set(t, Boolean.valueOf(val));
            } else if ("int".equalsIgnoreCase(fieldClassName) || "Integer".equals(fieldClassName)) {
                try {
                    field.set(t, Integer.valueOf(val));
                } catch (NumberFormatException e) {
                    errMsgList.add(String.format("[%s]的值格式不正确(当前值为%s)", cname, val));
                }
            } else if ("double".equalsIgnoreCase(fieldClassName)) {
                field.set(t, Double.valueOf(val));
            } else if ("long".equalsIgnoreCase(fieldClassName)) {
                field.set(t, Long.valueOf(val));
            } else if ("BigDecimal".equalsIgnoreCase(fieldClassName)) {
                field.set(t, new BigDecimal(val));
            } else if ("Date".equalsIgnoreCase(fieldClassName)) {
                try {
                    field.set(t, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(val));
                } catch (Exception e) {
                    field.set(t, new SimpleDateFormat("yyyy-MM-dd").parse(val));
                }
            } else if (field.getType().isEnum()) {
                // 处理枚举类型
                if (enumClass != null) {
                    setEnumFieldValue(t, field, val, enumClass, errMsgList);
                } else {
                    // 没有配置 @ExcelEnumField，尝试直接使用 val 作为枚举常量名
                    try {
                        @SuppressWarnings("unchecked")
                        Class<Enum> enumType = (Class<Enum>) field.getType();
                        field.set(t, Enum.valueOf(enumType, val));
                    } catch (Exception e) {
                        errMsgList.add(String.format("[%s]的枚举值不正确(当前值为%s)", cname, val));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建 @ExcelField 注解的适配器，使其能够像 @ExcelImport 一样使用
     *
     * @param excelField @ExcelField 注解实例
     * @return 适配器对象，行为类似于 @ExcelImport
     */
    private static ExcelImport createExcelImportAdapter(final ExcelField excelField) {
        return new ExcelImport() {
            @Override
            public String value() {
                return excelField.value();
            }

            @Override
            public String kv() {
                return excelField.kv();
            }

            @Override
            public boolean required() {
                return excelField.required();
            }

            @Override
            public int maxLength() {
                return excelField.maxLength();
            }

            @Override
            public boolean unique() {
                return excelField.unique();
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return ExcelImport.class;
            }
        };
    }

    /**
     * 根据字段类型进行值转换
     * <p>特殊处理枚举类型：将枚举常量名转换为枚举对象</p>
     *
     * @param t          目标 Java Bean 对象
     * @param field      当前处理的字段
     * @param val        要设置的值
     * @param enumClass  枚举类型 Class
     * @param errMsgList 错误信息列表
     */
    private static <T> void setEnumFieldValue(T t, Field field, String val, Class<? extends Enum<?>> enumClass, List<String> errMsgList) {
        try {
            // val 此时是枚举常量名（如：ENABLED），需要转换为枚举对象
            @SuppressWarnings("unchecked")
            Class<Enum> enumType = (Class<Enum>) enumClass.asSubclass(Enum.class);
            Enum<?> enumValue = Enum.valueOf(enumType, val);
            field.set(t, enumValue);
        } catch (Exception e) {
            String cname = field.getAnnotation(ExcelField.class).value();
            errMsgList.add(String.format("[%s]的值不正确(当前值为%s)", cname, val));
        }
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
