package com.infiext.soybean.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.infiext.soybean.domain.BasePO;
import com.infiext.soybean.enums.SendStatusEnum;
import com.infiext.soybean.enums.YesOrNoEnum;
import com.infiext.soybean.utils.excel.annotation.ExcelField;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 邮件发送日志 数据表的PO对象
 */
@Table("mail_send_log")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class MailSendLogPO extends BasePO<MailSendLogPO> {
    /**
     * 发件人
     */
    @Column(value = "mail_from")
    @ExcelField(value = "发件人", unique = false, sort = 7, required = false, maxLength = 500)
    private String mailFrom;

    /**
     * 收件人
     */
    @Column(value = "mail_to")
    @ExcelField(value = "收件人", unique = false, sort = 8, required = false, maxLength = 500)
    private String mailTo;

    /**
     * 抄送人
     */
    @Column(value = "mail_cc")
    @ExcelField(value = "抄送人", unique = false, sort = 9, required = false, maxLength = 500)
    private String mailCc;

    /**
     * 密送人
     */
    @Column(value = "mail_bcc")
    @ExcelField(value = "密送人", unique = false, sort = 10, required = false, maxLength = 500)
    private String mailBcc;

    /**
     * 邮件主题
     */
    @Column(value = "subject")
    @ExcelField(value = "邮件主题", unique = false, sort = 11, required = false, maxLength = 500)
    private String subject;

    /**
     * 邮件内容
     */
    @Column(value = "content")
    @ExcelField(value = "邮件内容", unique = false, sort = 12, required = false)
    private String content;

    /**
     * 是否HTML（0-否，1-是）
     */
    @Column(value = "is_html")
    @ExcelField(value = "是否HTML", unique = false, sort = 13, required = false, maxLength = 32)
    private YesOrNoEnum isHtml;

    /**
     * 发送状态（1-发送中，2-成功，3-失败）
     */
    @Column(value = "send_status")
    @ExcelField(value = "发送状态", unique = false, sort = 14, required = false, maxLength = 10)
    private SendStatusEnum sendStatus;

    /**
     * 错误信息
     */
    @Column(value = "error_message")
    @ExcelField(value = "错误信息", unique = false, sort = 15, required = false, maxLength = 500)
    private String errorMessage;

    /**
     * 发送时间
     */
    @Column(value = "send_time")
    @ExcelField(value = "发送时间", unique = false, sort = 16, required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sendTime;

}