package com.infiext.soybean.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.handler.ParamHandler;
import com.infiext.soybean.po.SysUserPO;
import com.infiext.soybean.service.SysUserService;
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
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService service;

    /**
     * 创建用户
     */
    @SaCheckPermission("sys:user:add")
    @PostMapping("/create")
    public SysUserPO create(@Validated @RequestBody SysUserPO po) {
        return service.create(po);
    }

    /**
     * 更新用户
     */
    @SaCheckPermission("sys:user:edit")
    @PostMapping("/update")
    public SysUserPO update(@Validated @RequestBody SysUserPO po) {
        return service.update(po);
    }

    /**
     * 逻辑删除用户
     */
    @SaCheckPermission("sys:user:delete")
    @PostMapping("/delete")
    public void delete(@RequestBody String[] ids) {
        service.delete(List.of(ids));
    }

    /**
     * 获取用户
     */
    @SaCheckPermission("sys:user:list")
    @PostMapping("/get")
    public SysUserPO get(@RequestParam String id) {
        return service.getById(id);
    }

    /**
     * 获取用户列表
     */
    @SaCheckPermission("sys:user:list")
    @PostMapping("/list")
    public List<SysUserPO> list(@RequestBody SysUserPO query,
                                @RequestParam(required = false) String columnKey,
                                @RequestParam(required = false) String order) {
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getList(query, sort);
    }

    /**
     * 获取用户分页
     */
    @SaCheckPermission("sys:user:list")
    @PostMapping("/page")
    public Page<SysUserPO> page(@RequestBody SysUserPO query,
                                @RequestParam(defaultValue = "1") Integer pageNumber,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String columnKey,
                                @RequestParam(required = false) String order) {
        Page<SysUserPO> page = ParamHandler.buildPage(pageNumber, pageSize);
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getPage(query, page, sort);
    }

    /**
     * 导出
     */
    @SaCheckPermission("sys:user:export")
    @PostMapping("/export")
    public void export(@RequestBody SysUserPO query,
                       @RequestParam(required = false) String columnKey,
                       @RequestParam(required = false) String order,
                       HttpServletResponse response) {
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        List<SysUserPO> list = service.getList(query, sort);
        ExcelUtils.export(response, "用户表", list, SysUserPO.class);
    }

    /**
     * 导入
     */
    @SaCheckPermission("sys:user:import")
    @PostMapping("/import")
    public void importData(@RequestParam("file") MultipartFile file) throws Exception {
        List<SysUserPO> pos = ExcelUtils.readMultipartFile(file, SysUserPO.class);
        service.createBatch(pos);
    }

    @PostMapping("/changePassword")
    public void changePassword(@RequestBody SysUserPO po) {
        service.updatePassword(po.getId(), po.getPassword());
    }
}
