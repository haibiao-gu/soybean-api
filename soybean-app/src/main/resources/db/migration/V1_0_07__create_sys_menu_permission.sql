DROP TABLE IF EXISTS sys_menu_permission;
CREATE TABLE sys_menu_permission
(
    menu_id VARCHAR(32) NOT NULL,
    key     VARCHAR(64) NOT NULL,
    value   VARCHAR(50)
);
COMMENT ON COLUMN sys_menu_permission.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_menu_permission.key IS '权限';
COMMENT ON COLUMN sys_menu_permission.value IS '描述';
COMMENT ON TABLE sys_menu_permission IS '菜单权限';

INSERT INTO sys_menu_permission (menu_id, "key", value)
VALUES ('2-2', 'sys:menu:add', '新增'),
       ('2-2', 'sys:menu:delete', '删除'),
       ('2-2', 'sys:menu:edit', '编辑'),
       ('2-2', 'sys:menu:list', '列表'),
       ('2-2', 'sys:menu:export', '导出'),
       ('2-2', 'sys:menu:import', '导入'),
       ('2-3', 'sys:role:add', '新增'),
       ('2-3', 'sys:role:delete', '删除'),
       ('2-3', 'sys:role:edit', '编辑'),
       ('2-3', 'sys:role:list', '列表'),
       ('2-3', 'sys:role:export', '导出'),
       ('2-3', 'sys:role:import', '导入'),
       ('2-1', 'sys:user:add', '新增'),
       ('2-1', 'sys:user:delete', '删除'),
       ('2-1', 'sys:user:edit', '编辑'),
       ('2-1', 'sys:user:list', '列表'),
       ('2-1', 'sys:user:export', '导出'),
       ('2-1', 'sys:user:import', '导入'),
       ('2-1', 'sys:user:resetPassword', '重置密码');
