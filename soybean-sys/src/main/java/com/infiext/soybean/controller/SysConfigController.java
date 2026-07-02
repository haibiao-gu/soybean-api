package com.infiext.soybean.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.infiext.soybean.enums.ConfigGroupEnum;
import com.infiext.soybean.po.SysConfigPO;
import com.infiext.soybean.service.SysConfigService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/sys/config")
public class SysConfigController {
    @Resource
    private SysConfigService service;

    @SaCheckPermission("sys:config:list")
    @PostMapping("/getByGroup")
    public List<SysConfigPO> getByGroup(@RequestParam ConfigGroupEnum group) {
        return service.getByGroup(group);
    }

    @SaCheckPermission("sys:config:edit")
    @PostMapping("/save")
    public void save(@RequestBody List<SysConfigPO> configs) {
        service.saveBatch(configs);
    }

    @SaCheckPermission("sys:config:test")
    @PostMapping("/testMail")
    public void testMail(@RequestBody Map<String, String> params) {
        String to = params.getOrDefault("to", "");
        service.testMail(to);
    }

    @SaCheckPermission("sys:config:test")
    @PostMapping("/testUpload")
    public void testUpload() {
        service.testUpload();
    }
}
