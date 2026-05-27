package com.infiext.soybean.controller;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.handler.ParamHandler;
import com.infiext.soybean.po.SysUserPO;
import com.infiext.soybean.service.SysUserService;
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
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService service;

    /**
     * 创建用户
     */
    @PostMapping("/create")
    public SysUserPO create(@Validated @RequestBody SysUserPO po) {
        return service.createSysUser(po);
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    public SysUserPO update(@Validated @RequestBody SysUserPO po) {
        return service.updateSysUser(po);
    }

    /**
     * 逻辑删除用户
     */
    @PostMapping("/delete")
    public void delete(@RequestBody String[] ids) {
        service.deleteSysUser(List.of(ids));
    }

    /**
     * 获取用户
     */
    @PostMapping("/get")
    public SysUserPO get(@RequestParam String id) {
        return service.getSysUserById(id);
    }

    /**
     * 获取用户列表
     */
    @PostMapping("/list")
    public List<SysUserPO> list(@RequestBody SysUserPO query,
                                @RequestParam(required = false) String columnKey,
                                @RequestParam(required = false) String order) {
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getSysUserList(query, sort);
    }

    /**
     * 获取用户分页
     */
    @PostMapping("/page")
    public Page<SysUserPO> page(@RequestBody SysUserPO query,
                                @RequestParam(defaultValue = "1") Integer pageNumber,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String columnKey,
                                @RequestParam(required = false) String order) {
        Page<SysUserPO> page = ParamHandler.buildPage(pageNumber, pageSize);
        SortParam sort = ParamHandler.buildSortParam(columnKey, order);
        return service.getSysUserPage(query, page, sort);
    }

    @PostMapping("/changePassword")
    public void changePassword(@RequestBody SysUserPO po) {
        service.updatePassword(po.getId(), po.getPassword());
    }
}
