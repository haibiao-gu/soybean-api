package com.infiext.soybean.utils.excel.writer;

import com.infiext.soybean.utils.excel.model.ExcelClassField;
import com.infiext.soybean.utils.excel.support.ExcelFieldExtractor;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Excel 文件写入器
 * <p>负责将 Java 数据导出为 Excel 文件，支持模板导出、数据导出、多 Sheet 页、图片插入和下拉列表等功能</p>
 *
 * @author system
 * @since 1.0
 */
public class ExcelWriter {

    private static final String XLSX = ".xlsx";
    private static final int IMG_HEIGHT = 30;
    private static final int IMG_WIDTH = 30;
    private static final char LEAN_LINE = '/';
    private static final int BYTES_DEFAULT_LENGTH = 10240;

    /**
     * 导出 Excel 模板（使用默认 Sheet 名称，不包含示例数据）
     *
     * @param response HTTP 响应对象
     * @param fileName 导出文件名
     * @param clazz    模板对应的 Java Bean 类型
     * @param <T>      泛型类型参数
     */
    public static <T> void exportTemplate(HttpServletResponse response, String fileName, Class<T> clazz) {
        exportTemplate(response, fileName, fileName, clazz, false);
    }

    /**
     * 导出 Excel 模板（指定 Sheet 名称，不包含示例数据）
     *
     * @param response  HTTP 响应对象
     * @param fileName  导出文件名
     * @param sheetName Sheet 页名称
     * @param clazz     模板对应的 Java Bean 类型
     * @param <T>       泛型类型参数
     */
    public static <T> void exportTemplate(HttpServletResponse response, String fileName, String sheetName, Class<T> clazz) {
        exportTemplate(response, fileName, sheetName, clazz, false);
    }

    /**
     * 导出 Excel 模板（使用默认 Sheet 名称，可选择是否包含示例数据）
     *
     * @param response         HTTP 响应对象
     * @param fileName         导出文件名
     * @param clazz            模板对应的 Java Bean 类型
     * @param isContainExample 是否包含示例数据行
     * @param <T>              泛型类型参数
     */
    public static <T> void exportTemplate(HttpServletResponse response, String fileName, Class<T> clazz, boolean isContainExample) {
        exportTemplate(response, fileName, fileName, clazz, isContainExample);
    }

    /**
     * 导出 Excel 模板（指定 Sheet 名称，可选择是否包含示例数据）
     * <p>根据 Java Bean 的注解配置生成表头、示例数据和下拉列表</p>
     *
     * @param response         HTTP 响应对象
     * @param fileName         导出文件名
     * @param sheetName        Sheet 页名称
     * @param clazz            模板对应的 Java Bean 类型
     * @param isContainExample 是否包含示例数据行
     * @param <T>              泛型类型参数
     */
    public static <T> void exportTemplate(HttpServletResponse response, String fileName, String sheetName, Class<T> clazz, boolean isContainExample) {
        List<ExcelClassField> headFieldList = ExcelFieldExtractor.getExcelClassFieldList(clazz);
        List<List<Object>> sheetDataList = new ArrayList<>();
        List<Object> headList = new ArrayList<>();
        List<Object> exampleList = new ArrayList<>();
        Map<Integer, List<String>> selectMap = new LinkedHashMap<>();
        for (int i = 0; i < headFieldList.size(); i++) {
            ExcelClassField each = headFieldList.get(i);
            headList.add(each.getName());
            exampleList.add(each.getExample());
            LinkedHashMap<String, String> kvMap = each.getKvMap();
            if (kvMap != null && !kvMap.isEmpty()) {
                selectMap.put(i, new ArrayList<>(kvMap.values()));
            }
        }
        sheetDataList.add(headList);
        if (isContainExample) {
            sheetDataList.add(exampleList);
        }
        export(response, fileName, sheetName, sheetDataList, selectMap);
    }

    /**
     * 导出 Excel 文件到本地（原始数据格式）
     *
     * @param file      本地文件对象
     * @param sheetData 表格数据，第一行为表头，后续行为数据行
     */
    public static void exportFile(File file, List<List<Object>> sheetData) {
        if (file == null) {
            System.out.println("文件创建失败");
            return;
        }
        if (sheetData == null) {
            sheetData = new ArrayList<>();
        }
        Map<String, List<List<Object>>> map = new HashMap<>();
        map.put(file.getName(), sheetData);
        export(null, file, file.getName(), map, null);
    }

    /**
     * 导出 Java Bean 列表到本地 Excel 文件
     *
     * @param filePath 文件父路径（如：D:/doc/excel/）
     * @param fileName 文件名称（不带尾缀，如：用户表）
     * @param list     导出的 Java Bean 列表
     * @param <T>      泛型类型参数
     * @return 生成的本地 File 文件对象
     * @throws IOException IO 异常
     */
    public static <T> File exportFile(String filePath, String fileName, List<T> list) throws IOException {
        File file = getFile(filePath, fileName);
        List<List<Object>> sheetData = getSheetData(list);
        exportFile(file, sheetData);
        return file;
    }

    /**
     * 导出空数据的 Excel 文件
     *
     * @param response HTTP 响应对象
     * @param fileName 导出文件名
     */
    public static void exportEmpty(HttpServletResponse response, String fileName) {
        List<List<Object>> sheetDataList = new ArrayList<>();
        List<Object> headList = new ArrayList<>();
        headList.add("导出无数据");
        sheetDataList.add(headList);
        export(response, fileName, sheetDataList);
    }

    /**
     * 导出 Excel 文件（使用默认 Sheet 名称）
     *
     * @param response    HTTP 响应对象
     * @param fileName    导出文件名
     * @param sheetDataList 表格数据，第一行为表头，后续行为数据行
     */
    public static void export(HttpServletResponse response, String fileName, List<List<Object>> sheetDataList) {
        export(response, fileName, fileName, sheetDataList, null);
    }

    /**
     * 导出多 Sheet 页的 Excel 文件
     *
     * @param response HTTP 响应对象
     * @param fileName 导出文件名
     * @param sheetMap Map 结构，key 为 Sheet 名称，value 为该 Sheet 的表格数据
     */
    public static void exportManySheet(HttpServletResponse response, String fileName, Map<String, List<List<Object>>> sheetMap) {
        export(response, null, fileName, sheetMap, null);
    }

    /**
     * 导出 Excel 文件（指定 Sheet 名称）
     *
     * @param response    HTTP 响应对象
     * @param fileName    导出文件名
     * @param sheetName   Sheet 页名称
     * @param sheetDataList 表格数据，第一行为表头，后续行为数据行
     */
    public static void export(HttpServletResponse response, String fileName, String sheetName, List<List<Object>> sheetDataList) {
        export(response, fileName, sheetName, sheetDataList, null);
    }

    /**
     * 导出 Excel 文件（指定 Sheet 名称和下拉列表）
     *
     * @param response    HTTP 响应对象
     * @param fileName    导出文件名
     * @param sheetName   Sheet 页名称
     * @param sheetDataList 表格数据，第一行为表头，后续行为数据行
     * @param selectMap   下拉列表配置，key 为列索引，value 为可选值列表
     */
    public static void export(HttpServletResponse response, String fileName, String sheetName, List<List<Object>> sheetDataList, Map<Integer, List<String>> selectMap) {
        Map<String, List<List<Object>>> map = new HashMap<>();
        map.put(sheetName, sheetDataList);
        export(response, null, fileName, map, selectMap);
    }

    /**
     * 导出 Java Bean 列表为 Excel 文件
     * <p>若列表为空且模板为空则导出空文件；若列表为空但模板不为空则导出模板</p>
     *
     * @param response HTTP 响应对象
     * @param fileName 导出文件名
     * @param list     导出的 Java Bean 列表
     * @param template 模板对应的 Java Bean 类型，用于生成表头和映射关系
     * @param <T>      导出数据类型
     * @param <K>      模板数据类型
     */
    public static <T, K> void export(HttpServletResponse response, String fileName, List<T> list, Class<K> template) {
        boolean lisIsEmpty = list == null || list.isEmpty();
        if (template == null && lisIsEmpty) {
            exportEmpty(response, fileName);
            return;
        }
        if (lisIsEmpty) {
            exportTemplate(response, fileName, template);
            return;
        }
        List<List<Object>> sheetDataList = getSheetData(list);
        export(response, fileName, sheetDataList);
    }

    /**
     * 导出 Excel 文件（指定下拉列表）
     *
     * @param response    HTTP 响应对象
     * @param fileName    导出文件名
     * @param sheetDataList 表格数据，第一行为表头，后续行为数据行
     * @param selectMap   下拉列表配置，key 为列索引，value 为可选值列表
     */
    public static void export(HttpServletResponse response, String fileName, List<List<Object>> sheetDataList, Map<Integer, List<String>> selectMap) {
        export(response, fileName, fileName, sheetDataList, selectMap);
    }

    /**
     * 核心导出方法：将数据写入 Excel 并输出到响应或本地文件
     * <p>支持多 Sheet 页、单元格合并、图片插入和下拉列表设置</p>
     *
     * @param response  HTTP 响应对象，为 null 时导出到本地文件
     * @param file      本地文件对象，response 不为 null 时可为 null
     * @param fileName  导出文件名
     * @param sheetMap  多 Sheet 页数据，key 为 Sheet 名称，value 为该 Sheet 的表格数据
     * @param selectMap 下拉列表配置，key 为列索引，value 为可选值列表
     */
    private static void export(HttpServletResponse response, File file, String fileName, Map<String, List<List<Object>>> sheetMap, Map<Integer, List<String>> selectMap) {
        SXSSFWorkbook book = new SXSSFWorkbook();
        Set<Map.Entry<String, List<List<Object>>>> entries = sheetMap.entrySet();
        for (Map.Entry<String, List<List<Object>>> entry : entries) {
            List<List<Object>> sheetDataList = entry.getValue();
            Sheet sheet = book.createSheet(entry.getKey());
            Drawing<?> patriarch = sheet.createDrawingPatriarch();
            CellStyle headStyle = ExcelStyleHelper.createHeadStyle(book);
            CellStyle rowStyle = ExcelStyleHelper.createRowStyle(book);
            sheet.setDefaultColumnWidth(15);

            // 创建合并算法数组并填充数据
            int rowLength = sheetDataList.size();
            int columnLength = sheetDataList.getFirst().size();
            int[][] mergeArray = new int[rowLength][columnLength];
            for (int i = 0; i < sheetDataList.size(); i++) {
                Row row = sheet.createRow(i);
                List<Object> rowList = sheetDataList.get(i);
                for (int j = 0; j < rowList.size(); j++) {
                    Object o = rowList.get(j);
                    int v = 0;
                    if (o instanceof URL) {
                        setCellPicture(book, row, patriarch, i, j, (URL) o);
                    } else {
                        Cell cell = row.createCell(j);
                        if (i == 0) {
                            v = ExcelStyleHelper.setCellValue(cell, o, headStyle);
                        } else {
                            v = ExcelStyleHelper.setCellValue(cell, o, rowStyle);
                        }
                    }
                    mergeArray[i][j] = v;
                }
            }

            // 合并单元格并设置下拉列表
            mergeCells(sheet, mergeArray);
            setSelect(sheet, selectMap);
        }

        // 根据目标输出方式写入数据
        if (response != null) {
            try {
                write(response, book, fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(file);
                ByteArrayOutputStream ops = new ByteArrayOutputStream();
                book.write(ops);
                fos.write(ops.toByteArray());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将 Java Bean 列表转换为 Excel 表格数据格式
     * <p>根据注解配置提取字段、转换枚举值并构建二维数据列表</p>
     *
     * @param list Java Bean 列表
     * @param <T>  泛型类型参数
     * @return 二维数据列表，第一行为表头，后续行为数据行
     */
    private static <T> List<List<Object>> getSheetData(List<T> list) {
        List<ExcelClassField> excelClassFieldList = ExcelFieldExtractor.getExcelClassFieldList(list.getFirst().getClass());
        List<String> headFieldList = new ArrayList<>();
        List<Object> headList = new ArrayList<>();
        Map<String, ExcelClassField> headFieldMap = new HashMap<>();
        for (ExcelClassField each : excelClassFieldList) {
            String fieldName = each.getFieldName();
            headFieldList.add(fieldName);
            headFieldMap.put(fieldName, each);
            headList.add(each.getName());
        }
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(headList);
        for (T t : list) {
            Map<String, Object> fieldDataMap = getFieldDataMap(t);
            Set<String> fieldDataKeys = fieldDataMap.keySet();
            List<Object> rowList = new ArrayList<>();
            for (String headField : headFieldList) {
                if (!fieldDataKeys.contains(headField)) {
                    continue;
                }
                Object data = fieldDataMap.get(headField);
                if (data == null) {
                    rowList.add("");
                    continue;
                }
                ExcelClassField cf = headFieldMap.get(headField);
                LinkedHashMap<String, String> kvMap = cf.getKvMap();
                if (kvMap == null || kvMap.isEmpty()) {
                    rowList.add(data);
                    continue;
                }
                String val = kvMap.get(data.toString());
                if (isNumeric(val)) {
                    rowList.add(Double.valueOf(val));
                } else {
                    rowList.add(val);
                }
            }
            sheetDataList.add(rowList);
        }
        return sheetDataList;
    }

    /**
     * 通过反射获取 Java Bean 的所有字段值
     *
     * @param t Java Bean 对象
     * @param <T> 泛型类型参数
     * @return 字段名与字段值的映射 Map
     */
    private static <T> Map<String, Object> getFieldDataMap(T t) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = t.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                String fieldName = field.getName();
                field.setAccessible(true);
                Object object = field.get(t);
                map.put(fieldName, object);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 根据合并标记数组执行单元格合并操作
     * <p>先处理横向合并（列合并），再处理纵向合并（行合并）</p>
     *
     * @param sheet      Excel Sheet 对象
     * @param mergeArray 合并标记数组，0-普通单元格，1-行合并标记，2-列合并标记
     */
    private static void mergeCells(Sheet sheet, int[][] mergeArray) {
        // 横向合并
        for (int x = 0; x < mergeArray.length; x++) {
            int[] arr = mergeArray[x];
            boolean merge = false;
            int y1 = 0;
            int y2 = 0;
            for (int y = 0; y < arr.length; y++) {
                int value = arr[y];
                if (value == 2) {
                    if (!merge) {
                        y1 = y;
                    }
                    y2 = y;
                    merge = true;
                } else {
                    merge = false;
                    if (y1 > 0) {
                        sheet.addMergedRegion(new CellRangeAddress(x, x, (y1 - 1), y2));
                    }
                    y1 = 0;
                    y2 = 0;
                }
            }
            if (y1 > 0) {
                sheet.addMergedRegion(new CellRangeAddress(x, x, (y1 - 1), y2));
            }
        }

        // 纵向合并
        int xLen = mergeArray.length;
        int yLen = mergeArray[0].length;
        for (int y = 0; y < yLen; y++) {
            boolean merge = false;
            int x1 = 0;
            int x2 = 0;
            for (int x = 0; x < xLen; x++) {
                int value = mergeArray[x][y];
                if (value == 1) {
                    if (!merge) {
                        x1 = x;
                    }
                    x2 = x;
                    merge = true;
                } else {
                    merge = false;
                    if (x1 > 0) {
                        sheet.addMergedRegion(new CellRangeAddress((x1 - 1), x2, y, y));
                    }
                    x1 = 0;
                    x2 = 0;
                }
            }
            if (x1 > 0) {
                sheet.addMergedRegion(new CellRangeAddress((x1 - 1), x2, y, y));
            }
        }
    }

    /**
     * 将 Excel 文件写入 HTTP 响应流
     *
     * @param response HTTP 响应对象
     * @param book     SXSSFWorkbook 对象
     * @param fileName 导出文件名
     * @throws IOException IO 异常
     */
    private static void write(HttpServletResponse response, SXSSFWorkbook book, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 使用 URLEncoder 进行文件名编码，兼容各种浏览器
        String encodedFileName = java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=" + encodedFileName + XLSX);

        ServletOutputStream out = response.getOutputStream();
        book.write(out);
        out.flush();
        out.close();
    }

    /**
     * 在单元格中插入图片
     *
     * @param wb         SXSSFWorkbook 对象
     * @param sr         当前行对象
     * @param patriarch  绘图对象
     * @param x          行索引
     * @param y          列索引
     * @param url        图片 URL 地址
     */
    private static void setCellPicture(SXSSFWorkbook wb, Row sr, Drawing<?> patriarch, int x, int y, URL url) {
        sr.setHeight((short) (IMG_WIDTH * IMG_HEIGHT));
        try (InputStream is = url.openStream(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buff = new byte[BYTES_DEFAULT_LENGTH];
            int rc;
            while ((rc = is.read(buff, 0, BYTES_DEFAULT_LENGTH)) > 0) {
                outputStream.write(buff, 0, rc);
            }
            org.apache.poi.xssf.usermodel.XSSFClientAnchor anchor = new org.apache.poi.xssf.usermodel.XSSFClientAnchor(0, 0, 0, 0, y, x, y + 1, x + 1);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            patriarch.createPicture(anchor, wb.addPicture(outputStream.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 为指定列设置下拉列表验证
     *
     * @param sheet     Excel Sheet 对象
     * @param selectMap 下拉列表配置，key 为列索引，value 为可选值列表
     */
    private static void setSelect(Sheet sheet, Map<Integer, List<String>> selectMap) {
        if (selectMap == null || selectMap.isEmpty()) {
            return;
        }
        Set<Map.Entry<Integer, List<String>>> entrySet = selectMap.entrySet();
        for (Map.Entry<Integer, List<String>> entry : entrySet) {
            int y = entry.getKey();
            List<String> list = entry.getValue();
            if (list == null || list.isEmpty()) {
                continue;
            }
            String[] arr = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i);
            }
            DataValidationHelper helper = sheet.getDataValidationHelper();
            CellRangeAddressList addressList = new CellRangeAddressList(1, 65000, y, y);
            DataValidationConstraint dvc = helper.createExplicitListConstraint(arr);
            DataValidation dv = helper.createValidation(dvc, addressList);
            if (dv instanceof HSSFDataValidation) {
                dv.setSuppressDropDownArrow(false);
            } else {
                dv.setSuppressDropDownArrow(true);
                dv.setShowErrorBox(true);
            }
            sheet.addValidationData(dv);
        }
    }

    /**
     * 创建本地文件对象，自动创建目录和文件
     *
     * @param filePath 文件父路径（如：D:/doc/excel/）
     * @param fileName 文件名称（不带尾缀，如：用户表）
     * @return 本地 File 文件对象，创建失败返回 null
     * @throws IOException IO 异常
     */
    private static File getFile(String filePath, String fileName) throws IOException {
        String dirPath = getString(filePath);
        String fileFullPath;
        if (dirPath.isEmpty()) {
            fileFullPath = fileName;
        } else {
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) {
                boolean mkdirs = dirFile.mkdirs();
                if (!mkdirs) {
                    return null;
                }
            }
            if (dirPath.endsWith(String.valueOf(LEAN_LINE))) {
                fileFullPath = dirPath + fileName + XLSX;
            } else {
                fileFullPath = dirPath + LEAN_LINE + fileName + XLSX;
            }
        }
        System.out.println(fileFullPath);
        File file = new File(fileFullPath);
        if (!file.exists()) {
            boolean result = file.createNewFile();
            if (!result) {
                return null;
            }
        }
        return file;
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
