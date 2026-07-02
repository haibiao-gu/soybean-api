DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user
(
    id          VARCHAR(32) NOT NULL,
    create_by   VARCHAR(32),
    create_time TIMESTAMP,
    update_by   VARCHAR(32),
    update_time TIMESTAMP,
    del_flag    INT4        NOT NULL,
    version     INT4        NOT NULL,
    user_name   VARCHAR(50) NOT NULL,
    user_phone  VARCHAR(64) NOT NULL,
    user_email  VARCHAR(500),
    user_avatar VARCHAR(500),
    password    VARCHAR(64),
    salt        VARCHAR(32),
    status      VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);
COMMENT ON COLUMN sys_user.id IS '主键';
COMMENT ON COLUMN sys_user.create_by IS '创建人';
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.update_by IS '更新人';
COMMENT ON COLUMN sys_user.update_time IS '更新时间';
COMMENT ON COLUMN sys_user.del_flag IS '删除标识';
COMMENT ON COLUMN sys_user.version IS '版本号';
COMMENT ON COLUMN sys_user.user_name IS '用户姓名';
COMMENT ON COLUMN sys_user.user_phone IS '用户手机号码';
COMMENT ON COLUMN sys_user.user_email IS '用户电子邮箱';
COMMENT ON COLUMN sys_user.user_avatar IS '用户头像';
COMMENT ON COLUMN sys_user.password IS '登录密码';
COMMENT ON COLUMN sys_user.salt IS '密码盐值';
COMMENT ON COLUMN sys_user.status IS '状态（1-正常，0-停用）';
COMMENT ON TABLE sys_user IS '用户表';

INSERT INTO sys_user (id, del_flag, "version", user_name, user_phone, "password", salt, status)
VALUES ('0', 0, 0, '系统管理员', 'admin', '$2b$12$YDaKJS5EVltPi2RSR7yW6ed9zIOJvaa/0zo44dhk5FLObFEeXv1Lq', 'a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6', '1');

INSERT INTO sys_menu (id, del_flag, "version", status, parent_id, menu_type, menu_name, route_name, route_path, component, icon, icon_type, keep_alive, constant, sort_order, hide_in_menu, multi_tab)
VALUES ('2-1', 0, 0, '1', '2', '2', '用户管理', 'system_user', '/system/user', 'view.system_user', 'ic:round-manage-accounts', '1', 1, 0, 1, 0, 0);

INSERT INTO sys_menu_permission (menu_id, "key", value)
VALUES ('2-1', 'sys:user:add', '新增'),
       ('2-1', 'sys:user:delete', '删除'),
       ('2-1', 'sys:user:edit', '编辑'),
       ('2-1', 'sys:user:list', '列表'),
       ('2-1', 'sys:user:export', '导出'),
       ('2-1', 'sys:user:import', '导入'),
       ('2-1', 'sys:user:resetPassword', '重置密码');

INSERT INTO sys_role_menu (role_id, menu_id)
VALUES ('0', '2-1');

INSERT INTO sys_role_permission (role_id, permission_key)
VALUES ('0', 'sys:user:add'),
       ('0', 'sys:user:delete'),
       ('0', 'sys:user:edit'),
       ('0', 'sys:user:list'),
       ('0', 'sys:user:export'),
       ('0', 'sys:user:import'),
       ('0', 'sys:user:resetPassword');
