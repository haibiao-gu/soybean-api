package com.infiext.soybean.mail.service.impl;

import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.mail.model.MailSendRequest;
import com.infiext.soybean.mail.service.MailService;
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
import java.util.List;

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
        } catch (MessagingException | MailException e) {
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
}
