DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role
(
    id          VARCHAR(32) NOT NULL,
    create_by   VARCHAR(32),
    create_time TIMESTAMP,
    update_by   VARCHAR(32),
    update_time TIMESTAMP,
    del_flag    INT4        NOT NULL,
    version     INT4        NOT NULL,
    role_name   VARCHAR(50),
    role_code   VARCHAR(64) NOT NULL,
    description VARCHAR(500),
    PRIMARY KEY (id)
);
COMMENT ON COLUMN sys_role.id IS '主键';
COMMENT ON COLUMN sys_role.create_by IS '创建人';
COMMENT ON COLUMN sys_role.create_time IS '创建时间';
COMMENT ON COLUMN sys_role.update_by IS '更新人';
COMMENT ON COLUMN sys_role.update_time IS '更新时间';
COMMENT ON COLUMN sys_role.del_flag IS '删除标识';
COMMENT ON COLUMN sys_role.version IS '版本号';
COMMENT ON COLUMN sys_role.role_name IS '角色名称';
COMMENT ON COLUMN sys_role.role_code IS '角色编码';
COMMENT ON COLUMN sys_role.description IS '角色描述';
COMMENT ON TABLE sys_role IS '系统角色';

INSERT INTO sys_role (id, del_flag, "version", role_name, role_code)
VALUES ('0', 0, 0, '系统管理员', 'ADMIN');
