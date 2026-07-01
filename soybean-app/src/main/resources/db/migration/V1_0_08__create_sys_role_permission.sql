DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission
(
    role_id        VARCHAR(32) NOT NULL,
    permission_key VARCHAR(64) NOT NULL
);
COMMENT ON COLUMN sys_role_permission.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_permission.permission_key IS '权限';
COMMENT ON TABLE sys_role_permission IS '角色权限关联表';

INSERT INTO sys_role_permission (role_id, permission_key)
VALUES ('0', 'sys:menu:add'),
       ('0', 'sys:menu:delete'),
       ('0', 'sys:menu:edit'),
       ('0', 'sys:menu:list'),
       ('0', 'sys:menu:export'),
       ('0', 'sys:menu:import'),
       ('0', 'sys:role:add'),
       ('0', 'sys:role:delete'),
       ('0', 'sys:role:edit'),
       ('0', 'sys:role:list'),
       ('0', 'sys:role:export'),
       ('0', 'sys:role:import'),
       ('0', 'sys:user:add'),
       ('0', 'sys:user:delete'),
       ('0', 'sys:user:edit'),
       ('0', 'sys:user:list'),
       ('0', 'sys:user:export'),
       ('0', 'sys:user:import');
