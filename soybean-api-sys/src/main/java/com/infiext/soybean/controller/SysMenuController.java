package com.infiext.soybean.controller;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.handler.ParamHandler;
import com.infiext.soybean.po.SysMenuPO;
import com.infiext.soybean.service.SysMenuService;
import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/create")
    public SysMenuPO create(@Validated @RequestBody SysMenuPO po) {
        return service.create(po);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public SysMenuPO update(@Validated @RequestBody SysMenuPO po) {
        return service.update(po);
    }

    /**
     * 逻辑删除
     */
    @PostMapping("/delete")
    public void delete(@RequestBody String[] ids) {
        service.deleteByIds(List.of(ids));
    }

    /**
     * 获取
     */
    @PostMapping("/get")
    public SysMenuPO get(@RequestParam String id) {
        return service.getById(id);
    }

    /**
     * 获取列表
     */
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