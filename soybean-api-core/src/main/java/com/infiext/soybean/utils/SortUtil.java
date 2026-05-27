package com.infiext.soybean.utils;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.enums.OrderEnum;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.query.QueryOrderBy;
import com.mybatisflex.core.query.QueryWrapper;

import java.lang.reflect.Field;

public class SortUtil {

    /**
     * 排序
     *
     * @param queryWrapper 查询包装器
     * @param param        排序参数
     * @param clazz        类
     * @param defaultOrder 默认排序
     */
    public static QueryWrapper orderBy(QueryWrapper queryWrapper, SortParam param, Class<?> clazz, QueryOrderBy defaultOrder) {
        if (param != null && param.getOrder() != OrderEnum.none) {
            boolean isAsc = param.getOrder() == OrderEnum.ascend;
            String columnValue = getColumnValueFromHierarchy(clazz, param.getColumnKey());
            if (columnValue != null) {
                return queryWrapper.orderBy(columnValue, isAsc);
            }
        }
        return queryWrapper.orderBy(defaultOrder);
    }

    /**
     * 根据属性名获取@Column注解的value值
     */
    public static String getColumnValue(Class<?> clazz, String fieldName) {
        try {
            // 获取指定字段
            Field field = clazz.getDeclaredField(fieldName);

            // 获取字段上的 @Column 注解
            Column columnAnnotation = field.getAnnotation(Column.class);

            // 返回注解的 value 值
            if (columnAnnotation != null) {
                return columnAnnotation.value();
            }
        } catch (NoSuchFieldException ignored) {
        }
        return null;
    }

    /**
     * 获取属性在类层次结构中的@Column注解的value值
     *
     * @param clazz     类
     * @param fieldName 属性名
     * @return @Column注解的value值
     */
    private static String getColumnValueFromHierarchy(Class<?> clazz, String fieldName) {
        String columnValue = getColumnValue(clazz, fieldName);
        if (columnValue != null) {
            return columnValue;
        }

        // 遍历父类查找属性
        Class<?> superClass = clazz.getSuperclass();
        while (superClass != null && superClass != Object.class) {
            columnValue = getColumnValue(superClass, fieldName);
            if (columnValue != null) {
                return columnValue;
            }
            superClass = superClass.getSuperclass();
        }

        return null;
    }
}