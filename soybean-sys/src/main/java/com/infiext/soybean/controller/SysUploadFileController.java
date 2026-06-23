package com.infiext.soybean.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.handler.ParamHandler;
import com.infiext.soybean.po.SysUploadFilePO;
import com.infiext.soybean.service.SysUploadFileService;
import com.infiext.soybean.utils.excel.ExcelUtils;
import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/sys/upload/file")
public class SysUploadFileController {
    @Resource
    private SysUploadFileService service;

    /**
     * 创建
     */
    @SaCheckPermission("sys:upload:file:add")
    @PostMapping("/create")
    public SysUploadFilePO create(@Validated @RequestBody SysUploadFilePO po) {
        return service.create(po);
    }

    /**
     * 更新
     */
    @SaCheckPermission("sys:upload:file:edit")
    @PostMapping("/update")
    public SysUploadFilePO update(@Validated @RequestBody SysUploadFilePO po) {
        return service.update(po);
    }

    /**
     * 逻辑删除
     */
    @SaCheckPermission("sys:upload:file:delete")
    @PostMapping("/delete")
    public void delete(@RequestBody String[] ids) {
        service.deleteByIds(List.of(ids));
    }

    /**
     * 获取
     */
    @SaCheckPermission("sys:upload:file:list")
    @PostMapping("/get")
    public SysUploadFilePO get(@RequestParam String id) {
        return service.getById(id);
    }

    /**
     * 获取列表
     */
    @SaCheckPermission("sys:upload:file:list")
    @PostMapping("/list")
    public List<SysUploadFilePO> list(@RequestBody SysUploadFilePO query,
                                      @RequestParam(required = false) String columnKey,
                                      @RequestParam(required = false) String order) {
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getList(query, sort);
    }

    /**
     * 获取分页
     */
    @SaCheckPermission("sys:upload:file:list")
    @PostMapping("/page")
    public Page<SysUploadFilePO> page(@RequestBody SysUploadFilePO query,
                                      @RequestParam(defaultValue = "1") Integer pageNumber,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestParam(required = false) String columnKey,
                                      @RequestParam(required = false) String order) {
        Page<SysUploadFilePO> page = ParamHandler.buildPage(pageNumber, pageSize);
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getPage(query, page, sort);
    }

    /**
     * 导出
     */
    @SaCheckPermission("sys:upload:file:export")
    @PostMapping("/export")
    public void export(@RequestBody SysUploadFilePO query,
                       @RequestParam(required = false) String columnKey,
                       @RequestParam(required = false) String order,
                       HttpServletResponse response) {
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        List<SysUploadFilePO> list = service.getList(query, sort);
        ExcelUtils.export(response, "上传文件管理", list, SysUploadFilePO.class);
    }

    /**
     * 导入
     */
    @SaCheckPermission("sys:upload:file:import")
    @PostMapping("/import")
    public void importData(@RequestParam("file") MultipartFile file) throws Exception {
        List<SysUploadFilePO> pos = ExcelUtils.readMultipartFile(file, SysUploadFilePO.class);
        service.createBatch(pos);
    }

}