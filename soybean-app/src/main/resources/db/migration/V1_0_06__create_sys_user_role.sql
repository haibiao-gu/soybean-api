DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role(
                              user_id VARCHAR(32) NOT NULL,
                              role_id VARCHAR(32) NOT NULL
);
COMMENT ON COLUMN sys_user_role.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role.role_id IS '角色ID';
COMMENT ON TABLE sys_user_role IS '用户角色关联表';

INSERT INTO sys_user_role (user_id, role_id)
VALUES ('0', '0');
