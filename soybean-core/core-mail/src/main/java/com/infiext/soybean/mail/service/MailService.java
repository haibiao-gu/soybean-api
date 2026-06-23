package com.infiext.soybean.mail.service;

import com.infiext.soybean.mail.model.MailSendRequest;

import java.util.List;

public interface MailService {
    void send(MailSendRequest request);

    default void sendText(String to, String subject, String content) {
        send(MailSendRequest.builder()
                .to(List.of(to))
                .subject(subject)
                .content(content)
                .html(false)
                .build());
    }
}
