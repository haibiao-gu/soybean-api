package com.infiext.soybean.utils.excel.reader;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.infiext.soybean.utils.excel.support.ExcelDataParser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Excel 文件读取器
 * <p>负责从本地文件或上传文件中读取 Excel 数据，支持 .xls 和 .xlsx 格式</p>
 * <p>提供单表和多表读取功能，将数据转换为 JSONArray 或 Java Bean 列表</p>
 *
 * @author system
 * @since 1.0
 */
public class ExcelReader {

    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";

    /**
     * 从本地文件读取 Excel 数据并转换为 Java Bean 列表
     *
     * @param file  本地 Excel 文件对象
     * @param clazz 目标 Java Bean 类型
     * @param <T>   泛型类型参数
     * @return 转换后的 Java Bean 列表
     * @throws Exception 文件读取或数据解析时可能抛出的异常
     */
    public static <T> List<T> readFile(File file, Class<T> clazz) throws Exception {
        JSONArray array = readExcel(null, file);
        return ExcelDataParser.getBeanList(array, clazz);
    }

    /**
     * 从上传文件读取 Excel 数据并转换为 Java Bean 列表
     *
     * @param mFile 上传的 MultipartFile 对象
     * @param clazz 目标 Java Bean 类型
     * @param <T>   泛型类型参数
     * @return 转换后的 Java Bean 列表
     * @throws Exception 文件读取或数据解析时可能抛出的异常
     */
    public static <T> List<T> readMultipartFile(MultipartFile mFile, Class<T> clazz) throws Exception {
        JSONArray array = readMultipartFile(mFile);
        return ExcelDataParser.getBeanList(array, clazz);
    }

    /**
     * 从本地文件读取 Excel 第一个 Sheet 页的数据
     *
     * @param file 本地 Excel 文件对象
     * @return 包含行数据的 JSONArray，每行数据为一个 JSONObject
     * @throws Exception 文件读取时可能抛出的异常
     */
    public static JSONArray readFile(File file) throws Exception {
        return readExcel(null, file);
    }

    /**
     * 从上传文件读取 Excel 第一个 Sheet 页的数据
     *
     * @param mFile 上传的 MultipartFile 对象
     * @return 包含行数据的 JSONArray，每行数据为一个 JSONObject
     * @throws Exception 文件读取时可能抛出的异常
     */
    public static JSONArray readMultipartFile(MultipartFile mFile) throws Exception {
        return readExcel(mFile, null);
    }

    /**
     * 从本地文件读取 Excel 所有 Sheet 页的数据
     *
     * @param file 本地 Excel 文件对象
     * @return Map 结构，key 为 Sheet 名称，value 为该 Sheet 的 JSONArray 数据
     * @throws Exception 文件读取时可能抛出的异常
     */
    public static Map<String, JSONArray> readFileManySheet(File file) throws Exception {
        return readExcelManySheet(null, file);
    }

    /**
     * 从上传文件读取 Excel 所有 Sheet 页的数据
     *
     * @param file 上传的 MultipartFile 对象
     * @return Map 结构，key 为 Sheet 名称，value 为该 Sheet 的 JSONArray 数据
     * @throws Exception 文件读取时可能抛出的异常
     */
    public static Map<String, JSONArray> readFileManySheet(MultipartFile file) throws Exception {
        return readExcelManySheet(file, null);
    }

    /**
     * 读取 Excel 所有 Sheet 页的数据（内部方法）
     *
     * @param mFile 上传文件对象，可为 null
     * @param file  本地文件对象，可为 null
     * @return Map 结构，key 为 Sheet 名称，value 为该 Sheet 的 JSONArray 数据
     * @throws IOException IO 异常
     */
    private static Map<String, JSONArray> readExcelManySheet(MultipartFile mFile, File file) throws IOException {
        Workbook book = getWorkbook(mFile, file);
        if (book == null) {
            return Collections.emptyMap();
        }
        Map<String, JSONArray> map = new LinkedHashMap<>();
        for (int i = 0; i < book.getNumberOfSheets(); i++) {
            Sheet sheet = book.getSheetAt(i);
            JSONArray arr = readSheet(sheet);
            map.put(sheet.getSheetName(), arr);
        }
        book.close();
        return map;
    }

    /**
     * 读取 Excel 第一个 Sheet 页的数据（内部方法）
     *
     * @param mFile 上传文件对象，可为 null
     * @param file  本地文件对象，可为 null
     * @return 包含行数据的 JSONArray
     * @throws IOException IO 异常
     */
    private static JSONArray readExcel(MultipartFile mFile, File file) throws IOException {
        Workbook book = getWorkbook(mFile, file);
        if (book == null) {
            return new JSONArray();
        }
        JSONArray array = readSheet(book.getSheetAt(0));
        book.close();
        return array;
    }

    /**
     * 根据文件类型创建 Workbook 对象
     * <p>支持 .xls（HSSF）和 .xlsx（XSSF）两种格式</p>
     *
     * @param mFile 上传文件对象，可为 null
     * @param file  本地文件对象，可为 null
     * @return Workbook 对象，文件格式不支持或文件不存在时返回 null
     * @throws IOException IO 异常
     */
    private static Workbook getWorkbook(MultipartFile mFile, File file) throws IOException {
        boolean fileNotExist = (file == null || !file.exists());
        if (mFile == null && fileNotExist) {
            return null;
        }
        InputStream in;
        String fileName;
        if (mFile != null) {
            in = mFile.getInputStream();
            fileName = getString(mFile.getOriginalFilename()).toLowerCase();
        } else {
            in = new FileInputStream(file);
            fileName = file.getName().toLowerCase();
        }
        Workbook book;
        if (fileName.endsWith(XLSX)) {
            book = new XSSFWorkbook(in);
        } else if (fileName.endsWith(XLS)) {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(in);
            book = new HSSFWorkbook(poifsFileSystem);
        } else {
            return null;
        }
        in.close();
        return book;
    }

    /**
     * 解析单个 Sheet 页的数据为 JSONArray
     * <p>第一行作为表头，后续行作为数据行，跳过空行</p>
     *
     * @param sheet Excel Sheet 对象
     * @return 包含行数据的 JSONArray，每个元素为 JSONObject
     */
    private static JSONArray readSheet(Sheet sheet) {
        int rowStart = sheet.getFirstRowNum();
        int rowEnd = sheet.getLastRowNum();
        Row headRow = sheet.getRow(rowStart);
        if (headRow == null) {
            return new JSONArray();
        }
        int cellStart = headRow.getFirstCellNum();
        int cellEnd = headRow.getLastCellNum();

        // 解析表头，建立列索引与列名的映射
        Map<Integer, String> keyMap = new HashMap<>();
        for (int j = cellStart; j < cellEnd; j++) {
            String val = getCellValue(headRow.getCell(j));
            if (val != null && !val.trim().isEmpty()) {
                keyMap.put(j, val);
            }
        }
        if (keyMap.isEmpty()) {
            return (JSONArray) Collections.emptyList();
        }

        // 解析数据行
        JSONArray array = new JSONArray();
        if (rowStart == rowEnd) {
            JSONObject obj = new JSONObject();
            obj.putOnce("rowNum", 1);
            for (int i : keyMap.keySet()) {
                obj.putOnce(keyMap.get(i), "");
            }
            array.add(obj);
            return array;
        }
        for (int i = rowStart + 1; i <= rowEnd; i++) {
            Row eachRow = sheet.getRow(i);
            JSONObject obj = new JSONObject();
            obj.putOnce("rowNum", i + 1);
            boolean hasData = false;
            for (int k = cellStart; k < cellEnd; k++) {
                if (eachRow != null) {
                    String val = getCellValue(eachRow.getCell(k));
                    if (!val.isEmpty()) {
                        hasData = true;
                    }
                    obj.putOnce(keyMap.get(k), val);
                }
            }
            if (hasData) {
                array.add(obj);
            }
        }
        return array;
    }

    /**
     * 获取单元格的值，自动处理不同数据类型
     * <p>支持字符串、数字、布尔值和公式类型</p>
     * <p>数字类型会自动去除末尾的 .0</p>
     * <p>长数字使用 DecimalFormat 避免科学计数法</p>
     *
     * @param cell Excel 单元格对象
     * @return 单元格的字符串值，空单元格返回空字符串
     */
    private static String getCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "";
        }
        if (cell.getCellType() == CellType.STRING) {
            String val = cell.getStringCellValue();
            if (val == null || val.trim().isEmpty()) {
                return "";
            }
            return val.trim();
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            double numericValue = cell.getNumericCellValue();
            // 使用 DecimalFormat 处理长数字，避免科学计数法
            if (Double.isNaN(numericValue) || Double.isInfinite(numericValue)) {
                return String.valueOf(numericValue);
            }

            // 判断是否为整数
            if (numericValue == Math.floor(numericValue) && !Double.isInfinite(numericValue)) {
                // 整数类型，使用 BigDecimal 避免精度丢失和科学计数法
                BigDecimal bd = new BigDecimal(numericValue);
                String result = bd.toPlainString();
                // 去除末尾的 .0（如果有的话）
                if (result.endsWith(".0")) {
                    result = result.substring(0, result.length() - 2);
                }
                return result;
            } else {
                // 小数类型，保留原始精度
                DecimalFormat df = new DecimalFormat("#.##########");
                return df.format(numericValue);
            }
        }
        if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue() + "";
        }
        return cell.getCellFormula();
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
