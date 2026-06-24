package com.infiext.soybean.service.impl;

import com.infiext.soybean.enums.SendStatusEnum;
import com.infiext.soybean.enums.YesOrNoEnum;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.model.MailSendRequest;
import com.infiext.soybean.service.MailService;
import com.infiext.soybean.po.MailSendLogPO;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SmtpMailService implements MailService {
    @Resource
    private JavaMailSender mailSender;

    @Value("${app.mail.from:}")
    private String defaultFrom;

    @Override
    public void send(MailSendRequest request) {
        validateRequest(request);
        String sender = StringUtils.hasText(request.getFrom()) ? request.getFrom() : defaultFrom;
        if (!StringUtils.hasText(sender)) {
            throw new BusinessException("邮件发件人不能为空，请配置 app.mail.from 或在请求中指定 from");
        }
        String[] toAddresses = toArray(request.getTo());
        if (toAddresses.length == 0) {
            throw new BusinessException("收件人不能为空");
        }
        MailSendLogPO logPO = buildLog(request, sender);
        logPO.save();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setFrom(sender);
            helper.setTo(toAddresses);
            if (!CollectionUtils.isEmpty(request.getCc())) {
                helper.setCc(toArray(request.getCc()));
            }
            if (!CollectionUtils.isEmpty(request.getBcc())) {
                helper.setBcc(toArray(request.getBcc()));
            }
            helper.setSubject(request.getSubject());
            helper.setText(request.getContent(), request.isHtml());
            mailSender.send(message);
            updateLogSuccess(logPO);
        } catch (MessagingException | MailException e) {
            updateLogFailure(logPO, e.getMessage());
            throw new BusinessException("发送邮件失败：" + e.getMessage());
        }
    }

    private void validateRequest(MailSendRequest request) {
        if (request == null) {
            throw new BusinessException("邮件请求不能为空");
        }
        if (CollectionUtils.isEmpty(request.getTo())) {
            throw new BusinessException("收件人不能为空");
        }
        if (!StringUtils.hasText(request.getSubject())) {
            throw new BusinessException("邮件主题不能为空");
        }
        if (!StringUtils.hasText(request.getContent())) {
            throw new BusinessException("邮件内容不能为空");
        }
    }

    private String[] toArray(List<String> addresses) {
        return addresses.stream().filter(StringUtils::hasText).toArray(String[]::new);
    }

    private MailSendLogPO buildLog(MailSendRequest request, String sender) {
        return MailSendLogPO.create()
                .setMailFrom(sender)
                .setMailTo(joinAddresses(request.getTo()))
                .setMailCc(joinAddresses(request.getCc()))
                .setMailBcc(joinAddresses(request.getBcc()))
                .setSubject(request.getSubject())
                .setContent(request.getContent())
                .setIsHtml(request.isHtml() ? YesOrNoEnum.Y : YesOrNoEnum.N)
                .setSendStatus(SendStatusEnum.SENDING)
                .setErrorMessage(null)
                .setSendTime(LocalDateTime.now());
    }

    private void updateLogSuccess(MailSendLogPO logPO) {
        logPO.setSendStatus(SendStatusEnum.SUCCESS);
        logPO.setErrorMessage(null);
        logPO.setSendTime(LocalDateTime.now());
        boolean status = logPO.updateById();
        if (!status) {
            throw new BusinessException("邮件发送成功但日志更新失败");
        }
    }

    private void updateLogFailure(MailSendLogPO logPO, String errorMessage) {
        logPO.setSendStatus(SendStatusEnum.FAILED);
        logPO.setErrorMessage(errorMessage);
        logPO.setSendTime(LocalDateTime.now());
        boolean status = logPO.updateById();
        if (!status) {
            throw new BusinessException("邮件发送失败且日志更新失败：" + errorMessage);
        }
    }

    private String joinAddresses(List<String> addresses) {
        if (CollectionUtils.isEmpty(addresses)) {
            return null;
        }
        return addresses.stream()
                .filter(StringUtils::hasText)
                .collect(Collectors.joining(","));
    }
}
