package com.infiext.soybean.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.handler.ParamHandler;
import com.infiext.soybean.po.SysRolePO;
import com.infiext.soybean.service.SysRoleService;
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
@RequestMapping("/sys/role")
public class SysRoleController {
    @Resource
    private SysRoleService service;

    /**
     * 创建
     */
    @SaCheckPermission("sys:role:add")
    @PostMapping("/create")
    public SysRolePO create(@Validated @RequestBody SysRolePO po) {
        return service.create(po);
    }

    /**
     * 更新
     */
    @SaCheckPermission("sys:role:edit")
    @PostMapping("/update")
    public SysRolePO update(@Validated @RequestBody SysRolePO po) {
        return service.update(po);
    }

    /**
     * 逻辑删除
     */
    @SaCheckPermission("sys:role:delete")
    @PostMapping("/delete")
    public void delete(@RequestBody String[] ids) {
        service.deleteByIds(List.of(ids));
    }

    /**
     * 获取
     */
    @SaCheckPermission("sys:role:list")
    @PostMapping("/get")
    public SysRolePO get(@RequestParam String id) {
        return service.getById(id);
    }

    /**
     * 获取列表
     */
    @SaCheckPermission("sys:role:list")
    @PostMapping("/list")
    public List<SysRolePO> list(@RequestBody SysRolePO query,
                                @RequestParam(required = false) String columnKey,
                                @RequestParam(required = false) String order) {
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getList(query, sort);
    }

    /**
     * 获取分页
     */
    @SaCheckPermission("sys:role:list")
    @PostMapping("/page")
    public Page<SysRolePO> page(@RequestBody SysRolePO query,
                                @RequestParam(defaultValue = "1") Integer pageNumber,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String columnKey,
                                @RequestParam(required = false) String order) {
        Page<SysRolePO> page = ParamHandler.buildPage(pageNumber, pageSize);
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getPage(query, page, sort);
    }

}