package com.infiext.soybean.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.handler.ParamHandler;
import com.infiext.soybean.po.SysMenuPO;
import com.infiext.soybean.service.SysMenuService;
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
@RequestMapping("/sys/menu")
public class SysMenuController {
    @Resource
    private SysMenuService service;

    /**
     * 创建
     */
    @SaCheckPermission("sys:menu:add")
    @PostMapping("/create")
    public SysMenuPO create(@Validated @RequestBody SysMenuPO po) {
        return service.create(po);
    }

    /**
     * 更新
     */
    @SaCheckPermission("sys:menu:edit")
    @PostMapping("/update")
    public SysMenuPO update(@Validated @RequestBody SysMenuPO po) {
        return service.update(po);
    }

    /**
     * 逻辑删除
     */
    @SaCheckPermission("sys:menu:delete")
    @PostMapping("/delete")
    public void delete(@RequestBody String[] ids) {
        service.deleteByIds(List.of(ids));
    }

    /**
     * 获取
     */
    @SaCheckPermission("sys:menu:list")
    @PostMapping("/get")
    public SysMenuPO get(@RequestParam String id) {
        return service.getById(id);
    }

    /**
     * 获取列表
     */
    @SaCheckPermission("sys:menu:list")
    @PostMapping("/list")
    public List<SysMenuPO> list(@RequestBody SysMenuPO query,
                                @RequestParam(required = false) String columnKey,
                                @RequestParam(required = false) String order) {
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getList(query, sort);
    }

    /**
     * 获取分页
     */
    @SaCheckPermission("sys:menu:list")
    @PostMapping("/page")
    public Page<SysMenuPO> page(@RequestBody SysMenuPO query,
                                @RequestParam(defaultValue = "1") Integer pageNumber,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String columnKey,
                                @RequestParam(required = false) String order) {
        Page<SysMenuPO> page = ParamHandler.buildPage(pageNumber, pageSize);
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getPage(query, page, sort);
    }

    /**
     * 导出
     */
    @SaCheckPermission("sys:menu:export")
    @PostMapping("/export")
    public void export(@RequestBody SysMenuPO query,
                       @RequestParam(required = false) String columnKey,
                       @RequestParam(required = false) String order,
                       HttpServletResponse response) {
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        List<SysMenuPO> list = service.getList(query, sort);
        ExcelUtils.export(response, "系统菜单", list, SysMenuPO.class);
    }

    /**
     * 导入
     */
    @SaCheckPermission("sys:menu:import")
    @PostMapping("/import")
    public void importData(@RequestParam("file") MultipartFile file) throws Exception {
        List<SysMenuPO> pos = ExcelUtils.readMultipartFile(file, SysMenuPO.class);
        service.createBatch(pos);
    }

    /**
     * 重置排序
     */
    @PostMapping("/resetSortOrder")
    public void resetSortOrder(@RequestBody String[] ids) {
        service.resetSortOrder(List.of(ids));
    }

    @PostMapping("/tree")
    public List<SysMenuPO> tree() {
        return service.tree();
    }
    
    @PostMapping("/allPages")
    public List<String> allPages() {
        return service.allPages();
    }
}