package com.infiext.soybean.utils.excel;

import cn.hutool.json.JSONArray;
import jakarta.servlet.http.HttpServletResponse;
import com.infiext.soybean.utils.excel.reader.ExcelReader;
import com.infiext.soybean.utils.excel.writer.ExcelWriter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Excel 工具类（门面类）
 * <p>提供统一的 Excel 导入导出 API，内部委托给专门的处理器完成具体功能</p>
 * <p>支持本地文件和上传文件的读取，支持单表和多表的数据导出</p>
 * <p>支持三种注解方式：</p>
 * <ul>
 *     <li>@ExcelField - 通用注解，同时支持导入和导出（推荐）</li>
 *     <li>@ExcelExport - 仅用于导出配置</li>
 *     <li>@ExcelImport - 仅用于导入配置</li>
 * </ul>
 *
 * @author system
 * @since 1.0
 */
@SuppressWarnings("unused")
public class ExcelUtils {

    /**
     * 行合并标记常量
     */
    public static final String ROW_MERGE = "row_merge";
    /**
     * 列合并标记常量
     */
    public static final String COLUMN_MERGE = "column_merge";

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
        return ExcelReader.readFile(file, clazz);
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
        return ExcelReader.readMultipartFile(mFile, clazz);
    }

    /**
     * 从本地文件读取 Excel 第一个 Sheet 页的数据
     *
     * @param file 本地 Excel 文件对象
     * @return 包含行数据的 JSONArray，每行数据为一个 JSONObject
     * @throws Exception 文件读取时可能抛出的异常
     */
    public static JSONArray readFile(File file) throws Exception {
        return ExcelReader.readFile(file);
    }

    /**
     * 从上传文件读取 Excel 第一个 Sheet 页的数据
     *
     * @param mFile 上传的 MultipartFile 对象
     * @return 包含行数据的 JSONArray，每行数据为一个 JSONObject
     * @throws Exception 文件读取时可能抛出的异常
     */
    public static JSONArray readMultipartFile(MultipartFile mFile) throws Exception {
        return ExcelReader.readMultipartFile(mFile);
    }

    /**
     * 从本地文件读取 Excel 所有 Sheet 页的数据
     *
     * @param file 本地 Excel 文件对象
     * @return Map 结构，key 为 Sheet 名称，value 为该 Sheet 的 JSONArray 数据
     * @throws Exception 文件读取时可能抛出的异常
     */
    public static Map<String, JSONArray> readFileManySheet(File file) throws Exception {
        return ExcelReader.readFileManySheet(file);
    }

    /**
     * 从上传文件读取 Excel 所有 Sheet 页的数据
     *
     * @param file 上传的 MultipartFile 对象
     * @return Map 结构，key 为 Sheet 名称，value 为该 Sheet 的 JSONArray 数据
     * @throws Exception 文件读取时可能抛出的异常
     */
    public static Map<String, JSONArray> readFileManySheet(MultipartFile file) throws Exception {
        return ExcelReader.readFileManySheet(file);
    }

    /**
     * 导出 Excel 模板（使用默认 Sheet 名称，不包含示例数据）
     *
     * @param response HTTP 响应对象
     * @param fileName 导出文件名
     * @param clazz    模板对应的 Java Bean 类型
     * @param <T>      泛型类型参数
     */
    public static <T> void exportTemplate(HttpServletResponse response, String fileName, Class<T> clazz) {
        ExcelWriter.exportTemplate(response, fileName, clazz);
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
        ExcelWriter.exportTemplate(response, fileName, sheetName, clazz);
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
        ExcelWriter.exportTemplate(response, fileName, clazz, isContainExample);
    }

    /**
     * 导出 Excel 模板（指定 Sheet 名称，可选择是否包含示例数据）
     *
     * @param response         HTTP 响应对象
     * @param fileName         导出文件名
     * @param sheetName        Sheet 页名称
     * @param clazz            模板对应的 Java Bean 类型
     * @param isContainExample 是否包含示例数据行
     * @param <T>              泛型类型参数
     */
    public static <T> void exportTemplate(HttpServletResponse response, String fileName, String sheetName, Class<T> clazz, boolean isContainExample) {
        ExcelWriter.exportTemplate(response, fileName, sheetName, clazz, isContainExample);
    }

    /**
     * 导出 Excel 文件到本地
     *
     * @param file      本地文件对象
     * @param sheetData 表格数据，第一行为表头，后续行为数据行
     */
    public static void exportFile(File file, List<List<Object>> sheetData) {
        ExcelWriter.exportFile(file, sheetData);
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
        return ExcelWriter.exportFile(filePath, fileName, list);
    }

    /**
     * 导出空数据的 Excel 文件
     *
     * @param response HTTP 响应对象
     * @param fileName 导出文件名
     */
    public static void exportEmpty(HttpServletResponse response, String fileName) {
        ExcelWriter.exportEmpty(response, fileName);
    }

    /**
     * 导出 Excel 文件（使用默认 Sheet 名称）
     *
     * @param response      HTTP 响应对象
     * @param fileName      导出文件名
     * @param sheetDataList 表格数据，第一行为表头，后续行为数据行
     */
    public static void export(HttpServletResponse response, String fileName, List<List<Object>> sheetDataList) {
        ExcelWriter.export(response, fileName, sheetDataList);
    }

    /**
     * 导出多 Sheet 页的 Excel 文件
     *
     * @param response HTTP 响应对象
     * @param fileName 导出文件名
     * @param sheetMap Map 结构，key 为 Sheet 名称，value 为该 Sheet 的表格数据
     */
    public static void exportManySheet(HttpServletResponse response, String fileName, Map<String, List<List<Object>>> sheetMap) {
        ExcelWriter.exportManySheet(response, fileName, sheetMap);
    }

    /**
     * 导出 Excel 文件（指定 Sheet 名称）
     *
     * @param response      HTTP 响应对象
     * @param fileName      导出文件名
     * @param sheetName     Sheet 页名称
     * @param sheetDataList 表格数据，第一行为表头，后续行为数据行
     */
    public static void export(HttpServletResponse response, String fileName, String sheetName, List<List<Object>> sheetDataList) {
        ExcelWriter.export(response, fileName, sheetName, sheetDataList);
    }

    /**
     * 导出 Excel 文件（指定 Sheet 名称和下拉列表）
     *
     * @param response      HTTP 响应对象
     * @param fileName      导出文件名
     * @param sheetName     Sheet 页名称
     * @param sheetDataList 表格数据，第一行为表头，后续行为数据行
     * @param selectMap     下拉列表配置，key 为列索引，value 为可选值列表
     */
    public static void export(HttpServletResponse response, String fileName, String sheetName, List<List<Object>> sheetDataList, Map<Integer, List<String>> selectMap) {
        ExcelWriter.export(response, fileName, sheetName, sheetDataList, selectMap);
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
        ExcelWriter.export(response, fileName, list, template);
    }

    /**
     * 导出 Excel 文件（指定下拉列表）
     *
     * @param response      HTTP 响应对象
     * @param fileName      导出文件名
     * @param sheetDataList 表格数据，第一行为表头，后续行为数据行
     * @param selectMap     下拉列表配置，key 为列索引，value 为可选值列表
     */
    public static void export(HttpServletResponse response, String fileName, List<List<Object>> sheetDataList, Map<Integer, List<String>> selectMap) {
        ExcelWriter.export(response, fileName, sheetDataList, selectMap);
    }
}
