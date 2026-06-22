package com.infiext.soybean.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.infiext.soybean.service.RouteService;
import com.infiext.soybean.vo.RouteVO;
import com.infiext.soybean.vo.UserRoleVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/route")
public class RouteController {
    @Resource
    private RouteService service;

    @SaCheckLogin
    @PostMapping("/getUserRoutes")
    public UserRoleVO getUserRoutes() {
        String userId = StpUtil.getLoginIdAsString();
        return service.getUserRoutes(userId);
    }

    @PostMapping("/getConstantRoutes")
    public List<RouteVO> getConstantRoutes() {
        return service.getConstantRoutes();
    }
}
