DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu
(
    role_id VARCHAR(32) NOT NULL,
    menu_id VARCHAR(32) NOT NULL
);
COMMENT ON COLUMN sys_role_menu.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_menu.menu_id IS '菜单ID';
COMMENT ON TABLE sys_role_menu IS '角色菜单关联表';

INSERT INTO sys_role_menu (role_id, menu_id)
VALUES ('0', '1'),
       ('0', '2'),
       ('0', '2-2'),
       ('0', '2-3');
