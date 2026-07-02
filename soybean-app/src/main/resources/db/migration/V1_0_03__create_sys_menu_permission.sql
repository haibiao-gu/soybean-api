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
       ('2-2', 'sys:menu:import', '导入');
