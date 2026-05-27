package com.infiext.soybean.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.infiext.soybean.domain.TestBO;
import com.infiext.soybean.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/success")
    public String success() {
        return "success";
    }

    @PostMapping("/error")
    public Integer error() {
        return 1 / 0;
    }

    @GetMapping("/get")
    public String hello(@RequestParam String data) {
        return data;
    }

    @PostMapping("/post")
    public TestBO post(@RequestBody TestBO data) {
        return data;
    }

    @PostMapping("/postparam")
    public TestBO postParam(@RequestParam String param, @RequestBody TestBO data) {
        return data;
    }

    @PostMapping("/nolog")
    public void noLog() {
    }

    @PostMapping("/exception")
    public void exception() {
        throw new BusinessException("测试异常");
    }

    @SaCheckLogin
    @GetMapping("/checklogin")
    public String checkLogin() {
        return "已登录";
    }
}
