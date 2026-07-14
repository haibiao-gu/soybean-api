DROP TABLE IF EXISTS sys_config;
CREATE TABLE sys_config
(
    id           VARCHAR(32)  NOT NULL,
    create_by    VARCHAR(32),
    create_time  TIMESTAMP,
    update_by    VARCHAR(32),
    update_time  TIMESTAMP,
    del_flag     INT4         NOT NULL,
    version      INT4         NOT NULL,
    config_group VARCHAR(64)  NOT NULL,
    config_key   VARCHAR(64) NOT NULL,
    config_value TEXT,
    description  VARCHAR(500),
    PRIMARY KEY (id)
);
COMMENT ON COLUMN sys_config.id IS '主键';
COMMENT ON COLUMN sys_config.create_by IS '创建人';
COMMENT ON COLUMN sys_config.create_time IS '创建时间';
COMMENT ON COLUMN sys_config.update_by IS '更新人';
COMMENT ON COLUMN sys_config.update_time IS '更新时间';
COMMENT ON COLUMN sys_config.del_flag IS '删除标识';
COMMENT ON COLUMN sys_config.version IS '版本号';
COMMENT ON COLUMN sys_config.config_group IS '配置分组（MAIL-邮箱 UPLOAD-上传）';
COMMENT ON COLUMN sys_config.config_key IS '配置键';
COMMENT ON COLUMN sys_config.config_value IS '配置值';
COMMENT ON COLUMN sys_config.description IS '配置说明';
COMMENT ON TABLE sys_config IS '系统配置';

INSERT INTO sys_config (id, del_flag, "version", config_group, config_key, config_value, description)
VALUES ('cf-mail-01', 0, 0, 'MAIL', 'host', '', 'SMTP服务器地址'),
       ('cf-mail-02', 0, 0, 'MAIL', 'port', '25', 'SMTP端口'),
       ('cf-mail-03', 0, 0, 'MAIL', 'username', '', '邮箱账号'),
       ('cf-mail-04', 0, 0, 'MAIL', 'password', '', '邮箱密码/授权码'),
       ('cf-mail-05', 0, 0, 'MAIL', 'smtp_auth', 'false', '是否开启SMTP认证'),
       ('cf-mail-06', 0, 0, 'MAIL', 'starttls_enable', 'false', '是否启用STARTTLS'),
       ('cf-mail-07', 0, 0, 'MAIL', 'from', '', '发件人地址'),
       ('cf-upload-01', 0, 0, 'UPLOAD', 'store_type', 'LOCAL', '存储类型（LOCAL/MINIO）'),
       ('cf-upload-02', 0, 0, 'UPLOAD', 'local_dir', './uploads', '本地上传目录'),
       ('cf-upload-03', 0, 0, 'UPLOAD', 'minio_endpoint', '', 'MinIO服务端点'),
       ('cf-upload-04', 0, 0, 'UPLOAD', 'minio_access_key', '', 'MinIO访问密钥'),
       ('cf-upload-05', 0, 0, 'UPLOAD', 'minio_secret_key', '', 'MinIO秘密密钥'),
       ('cf-upload-06', 0, 0, 'UPLOAD', 'minio_bucket_name', '', 'MinIO桶名称'),
       ('cf-upload-07', 0, 0, 'UPLOAD', 'minio_public_url', '', 'MinIO公开访问地址');

INSERT INTO sys_menu (id, del_flag, "version", status, parent_id, menu_type, menu_name, route_name, route_path, component, icon, icon_type, keep_alive, constant, sort_order, hide_in_menu, multi_tab)
VALUES ('2-4', 0, 0, '1', '2', '2', '邮箱配置', 'system_config_email', '/system/config/email', 'view.system_config_email', 'mdi:email-outline', '1', 1, 0, 4, 0, 0),
       ('2-5', 0, 0, '1', '2', '2', '上传配置', 'system_config_upload', '/system/config/upload', 'view.system_config_upload', 'mdi:cloud-upload-outline', '1', 1, 0, 5, 0, 0);

INSERT INTO sys_menu_permission (menu_id, "key", value)
VALUES ('2-4', 'sys:config:list', '列表'),
       ('2-4', 'sys:config:edit', '编辑'),
       ('2-4', 'sys:config:test', '测试'),
       ('2-5', 'sys:config:list', '列表'),
       ('2-5', 'sys:config:edit', '编辑'),
       ('2-5', 'sys:config:test', '测试');

INSERT INTO sys_role_menu (role_id, menu_id)
VALUES ('0', '2-4'),
       ('0', '2-5');

INSERT INTO sys_role_permission (role_id, permission_key)
VALUES ('0', 'sys:config:list'),
       ('0', 'sys:config:edit'),
       ('0', 'sys:config:test');
