package com.infiext.soybean.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.infiext.soybean.domain.TestBO;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.model.MailSendRequest;
import com.infiext.soybean.po.UploadFilePO;
import com.infiext.soybean.service.MailService;
import com.infiext.soybean.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/test")
@Profile("dev")
public class TestController {
    @Resource
    private MailService mailService;
    @Resource
    private UploadFileService uploadFileService;

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

    @PostMapping("/mail/send")
    public String sendMail(@RequestParam String to,
                           @RequestParam(defaultValue = "Soybean 测试邮件") String subject,
                           @RequestParam(defaultValue = "这是一封测试邮件。") String content,
                           @RequestParam(defaultValue = "false") boolean html) {
        List<String> recipients = Arrays.stream(to.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
        mailService.send(MailSendRequest.builder()
                .to(recipients)
                .subject(subject)
                .content(content)
                .html(html)
                .build());
        return "success";
    }

    @PostMapping("/upload/file")
    public UploadFilePO uploadFile(@RequestParam("file") MultipartFile file,
                                   @RequestParam(defaultValue = "test") String bizType,
                                   @RequestParam(defaultValue = "test") String bizId) {
        return uploadFileService.uploadFile(file, bizType, bizId);
    }

    @SaCheckLogin
    @GetMapping("/checklogin")
    public String checkLogin() {
        return "已登录";
    }
}
