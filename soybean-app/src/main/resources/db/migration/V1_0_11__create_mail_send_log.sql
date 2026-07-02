DROP TABLE IF EXISTS mail_send_log;
CREATE TABLE mail_send_log
(
    id            VARCHAR(32) NOT NULL,
    create_by     VARCHAR(32),
    create_time   TIMESTAMP,
    update_by     VARCHAR(32),
    update_time   TIMESTAMP,
    del_flag      INT4        NOT NULL,
    version       INT4        NOT NULL,
    mail_from     VARCHAR(500),
    mail_to       VARCHAR(500),
    mail_cc       VARCHAR(500),
    mail_bcc      VARCHAR(500),
    subject       VARCHAR(500),
    content       TEXT,
    is_html       INT4,
    send_status   VARCHAR(10),
    error_message VARCHAR(500),
    send_time     TIMESTAMP,
    PRIMARY KEY (id)
);
COMMENT ON COLUMN mail_send_log.id IS '主键';
COMMENT ON COLUMN mail_send_log.create_by IS '创建人';
COMMENT ON COLUMN mail_send_log.create_time IS '创建时间';
COMMENT ON COLUMN mail_send_log.update_by IS '更新人';
COMMENT ON COLUMN mail_send_log.update_time IS '更新时间';
COMMENT ON COLUMN mail_send_log.del_flag IS '删除标识';
COMMENT ON COLUMN mail_send_log.version IS '版本号';
COMMENT ON COLUMN mail_send_log.mail_from IS '发件人';
COMMENT ON COLUMN mail_send_log.mail_to IS '收件人';
COMMENT ON COLUMN mail_send_log.mail_cc IS '抄送人';
COMMENT ON COLUMN mail_send_log.mail_bcc IS '密送人';
COMMENT ON COLUMN mail_send_log.subject IS '邮件主题';
COMMENT ON COLUMN mail_send_log.content IS '邮件内容';
COMMENT ON COLUMN mail_send_log.is_html IS '是否HTML（0-否，1-是）';
COMMENT ON COLUMN mail_send_log.send_status IS '发送状态（1-发送中，2-成功，3-失败）';
COMMENT ON COLUMN mail_send_log.error_message IS '错误信息';
COMMENT ON COLUMN mail_send_log.send_time IS '发送时间';
COMMENT ON TABLE mail_send_log IS '邮件发送日志';

