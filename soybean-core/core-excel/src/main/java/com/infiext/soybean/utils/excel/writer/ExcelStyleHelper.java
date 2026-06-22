package com.infiext.soybean.utils.excel.writer;

import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Excel 样式助手
 * <p>提供 Excel 单元格样式创建、值设置和合并标记功能</p>
 *
 * @author system
 * @since 1.0
 */
public class ExcelStyleHelper {

    private static final String ROW_MERGE = "row_merge";
    private static final String COLUMN_MERGE = "column_merge";
    private static final int CELL_OTHER = 0;
    private static final int CELL_ROW_MERGE = 1;
    private static final int CELL_COLUMN_MERGE = 2;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 创建表头单元格样式
     * <p>灰色背景，居中对齐</p>
     *
     * @param book Workbook 对象
     * @return 表头单元格样式对象
     */
    public static CellStyle createHeadStyle(Workbook book) {
        CellStyle headStyle = book.createCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.index);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        return headStyle;
    }

    /**
     * 创建数据行单元格样式
     * <p>水平居中对齐，垂直居中对齐</p>
     *
     * @param book Workbook 对象
     * @return 数据行单元格样式对象
     */
    public static CellStyle createRowStyle(Workbook book) {
        CellStyle rowStyle = book.createCellStyle();
        rowStyle.setAlignment(HorizontalAlignment.CENTER);
        rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return rowStyle;
    }

    /**
     * 设置单元格值并应用样式
     * <p>根据数据类型自动转换并设置单元格值，支持字符串、数字、布尔值、BigDecimal 和日期类型</p>
     * <p>特殊字符串 "row_merge" 和 "column_merge" 用于标记单元格合并</p>
     * <p>长数字使用字符串形式避免科学计数法</p>
     *
     * @param cell  目标单元格对象
     * @param o     要设置的值对象
     * @param style 单元格样式对象
     * @return 单元格合并标记：0-普通单元格，1-行合并标记，2-列合并标记
     */
    public static int setCellValue(Cell cell, Object o, CellStyle style) {
        cell.setCellStyle(style);
        if (o == null) {
            cell.setCellValue("");
            return CELL_OTHER;
        }
        if (o instanceof String) {
            String s = o.toString();
            if (!s.isEmpty() && isNumeric(s) && s.length() < 8) {
                cell.setCellValue(Double.parseDouble(s));
                return CELL_OTHER;
            } else {
                cell.setCellValue(s);
            }
            if (s.equals(ROW_MERGE)) {
                return CELL_ROW_MERGE;
            } else if (s.equals(COLUMN_MERGE)) {
                return CELL_COLUMN_MERGE;
            } else {
                return CELL_OTHER;
            }
        }
        if (o instanceof Integer || o instanceof Long || o instanceof Double || o instanceof Float) {
            // 对于长数字类型，转换为字符串形式避免科学计数法
            String numStr = formatNumber(o);
            cell.setCellValue(numStr);
            return CELL_OTHER;
        }
        switch (o) {
            case Boolean b -> {
                cell.setCellValue(b);
                return CELL_OTHER;
            }
            case BigDecimal bigDecimal -> {
                // BigDecimal 直接转为字符串，保持精度
                cell.setCellValue(bigDecimal.toPlainString());
                return CELL_OTHER;
            }
            case Date date -> {
                cell.setCellValue(formatDate(date));
                return CELL_OTHER;
            }
            default -> {
            }
        }
        cell.setCellValue(o.toString());
        return CELL_OTHER;
    }

    /**
     * 格式化数字为字符串，避免科学计数法
     * <p>对于长数字（超过11位）或包含小数点的数字，使用字符串形式存储</p>
     *
     * @param number 数字对象
     * @return 格式化后的字符串表示
     */
    private static String formatNumber(Object number) {
        if (number == null) {
            return "";
        }
        
        // 如果是 Long 类型且数值较大，直接转为字符串
        if (number instanceof Long) {
            return number.toString();
        }
        
        // 对于其他数字类型，先转为 BigDecimal 再转字符串
        try {
            BigDecimal bd = new BigDecimal(number.toString());
            String result = bd.toPlainString();
            // 如果是整数形式（如 123.0），去掉小数部分
            if (result.endsWith(".0")) {
                result = result.substring(0, result.length() - 2);
            }
            return result;
        } catch (Exception e) {
            // 异常情况下使用默认 toString
            return number.toString();
        }
    }

    /**
     * 格式化日期对象为字符串
     *
     * @param date 日期对象
     * @return 格式化后的日期字符串，格式为 "yyyy-MM-dd HH:mm:ss"，null 返回空字符串
     */
    private static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }

    /**
     * 判断字符串是否为纯数字
     *
     * @param str 待检查的字符串
     * @return true 表示是数字，false 表示不是数字
     */
    private static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if ("0.0".equals(str)) {
            return true;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

