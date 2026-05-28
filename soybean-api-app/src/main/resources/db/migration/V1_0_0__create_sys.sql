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
    user_avatar VARCHAR(500),
    password    VARCHAR(64),
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
COMMENT ON COLUMN sys_user.user_avatar IS '用户头像';
COMMENT ON COLUMN sys_user.password IS '登录密码';
COMMENT ON COLUMN sys_user.status IS '状态（1-正常，0-停用）';
COMMENT ON TABLE sys_user IS '用户表';


DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu
(
    id                 VARCHAR(32)  NOT NULL,
    create_by          VARCHAR(32),
    create_time        TIMESTAMP,
    update_by          VARCHAR(32),
    update_time        TIMESTAMP,
    del_flag           INT4         NOT NULL,
    version            INT4         NOT NULL,
    status             VARCHAR(10)  NOT NULL,
    parent_id          VARCHAR(32)  NOT NULL,
    menu_type          VARCHAR(10)  NOT NULL,
    menu_name          VARCHAR(50)  NOT NULL,
    route_name         VARCHAR(50)  NOT NULL,
    route_path         VARCHAR(500) NOT NULL,
    component          VARCHAR(500),
    icon               VARCHAR(64),
    icon_type          VARCHAR(10),
    i18n_key           VARCHAR(50),
    keep_alive         INT4,
    constant           INT4,
    sort_order         INT4,
    href               VARCHAR(500),
    hide_in_menu       INT4,
    active_menu        VARCHAR(50),
    multi_tab          INT4,
    fixed_index_in_tab INT4,
    PRIMARY KEY (id)
);
COMMENT ON COLUMN sys_menu.id IS '主键';
COMMENT ON COLUMN sys_menu.create_by IS '创建人';
COMMENT ON COLUMN sys_menu.create_time IS '创建时间';
COMMENT ON COLUMN sys_menu.update_by IS '更新人';
COMMENT ON COLUMN sys_menu.update_time IS '更新时间';
COMMENT ON COLUMN sys_menu.del_flag IS '删除标识';
COMMENT ON COLUMN sys_menu.version IS '版本号';
COMMENT ON COLUMN sys_menu.status IS '状态（1-正常，0-停用）';
COMMENT ON COLUMN sys_menu.parent_id IS '父菜单ID（0表示根菜单）';
COMMENT ON COLUMN sys_menu.menu_type IS '菜单类型（1:目录 2:菜单）';
COMMENT ON COLUMN sys_menu.menu_name IS '菜单名称';
COMMENT ON COLUMN sys_menu.route_name IS '路由名称';
COMMENT ON COLUMN sys_menu.route_path IS '路由路径';
COMMENT ON COLUMN sys_menu.component IS '组件路径';
COMMENT ON COLUMN sys_menu.icon IS '图标（iconify图标名或本地图标名）';
COMMENT ON COLUMN sys_menu.icon_type IS '图标类型（1:iconify图标 2:本地图标）';
COMMENT ON COLUMN sys_menu.i18n_key IS '国际化key';
COMMENT ON COLUMN sys_menu.keep_alive IS '是否缓存（0:否 1:是）';
COMMENT ON COLUMN sys_menu.constant IS '是否常量路由（0:否 1:是）';
COMMENT ON COLUMN sys_menu.sort_order IS '排序号';
COMMENT ON COLUMN sys_menu.href IS '外链地址';
COMMENT ON COLUMN sys_menu.hide_in_menu IS '是否在菜单中隐藏（0:否 1:是）';
COMMENT ON COLUMN sys_menu.active_menu IS '激活的菜单（用于高亮）';
COMMENT ON COLUMN sys_menu.multi_tab IS '是否支持多标签（0:否 1:是）';
COMMENT ON COLUMN sys_menu.fixed_index_in_tab IS '在tab中的固定索引';
COMMENT ON TABLE sys_menu IS '系统菜单';


DROP TABLE IF EXISTS sys_menu_query;
CREATE TABLE sys_menu_query
(
    menu_id VARCHAR(32) NOT NULL,
    key     VARCHAR(64) NOT NULL,
    value   VARCHAR(64) NOT NULL
);
COMMENT ON COLUMN sys_menu_query.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_menu_query.key IS '参数名';
COMMENT ON COLUMN sys_menu_query.value IS '参数值';
COMMENT ON TABLE sys_menu_query IS '菜单路由查询参数';


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


DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu
(
    role_id VARCHAR(32) NOT NULL,
    menu_id VARCHAR(32) NOT NULL
);
COMMENT ON COLUMN sys_role_menu.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_menu.menu_id IS '菜单ID';
COMMENT ON TABLE sys_role_menu IS '角色菜单关联表';


DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission
(
    role_id        VARCHAR(32) NOT NULL,
    permission_key VARCHAR(64) NOT NULL
);
COMMENT ON COLUMN sys_role_permission.role_id IS '角色ID';
COMMENT ON COLUMN sys_role_permission.permission_key IS '权限';
COMMENT ON TABLE sys_role_permission IS '角色权限关联表';


DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role
(
    user_id VARCHAR(32) NOT NULL,
    role_id VARCHAR(32) NOT NULL
);
COMMENT ON COLUMN sys_user_role.user_id IS '用户ID';
COMMENT ON COLUMN sys_user_role.role_id IS '角色ID';
COMMENT ON TABLE sys_user_role IS '用户角色关联表';


INSERT INTO sys_user (id, del_flag, "version", user_name, user_phone, "password", status)
VALUES ('0', 0, 0, '管理员', 'admin', '49ba59abbe56e057', '1');


INSERT INTO sys_role (id, del_flag, "version", role_name, role_code)
VALUES ('0', 0, 0, '管理员', 'ADMIN');


INSERT INTO sys_user_role (user_id, role_id)
VALUES ('0', '0');


INSERT INTO sys_menu (id, del_flag, "version", status, parent_id, menu_type, menu_name, route_name, route_path, component, icon, icon_type, keep_alive, constant, sort_order, hide_in_menu, multi_tab)
VALUES ('1', 0, 0, '1', '0', '2', '首页', 'home', '/home', 'layout.base$view.home', 'mdi:monitor-dashboard', '1', 1, 0, 1, 0, 0),
       ('2', 0, 0, '1', '0', '1', '系统设定', 'system', '/system', 'layout.base', 'carbon:cloud-service-management', '1', 0, 0, 5, 0, 0),
       ('2-1', 0, 0, '1', '2', '2', '用户管理', 'system_user', '/system/user', 'view.system_user', 'ic:round-manage-accounts', '1', 1, 0, 1, 0, 0),
       ('2-2', 0, 0, '1', '2', '2', '菜单管理', 'system_menu', '/system/menu', 'view.system_menu', 'material-symbols:route', '1', 1, 0, 2, 0, 0),
       ('2-3', 0, 0, '1', '2', '2', '角色管理', 'system_role', '/system/role', 'view.system_role', 'carbon:user-role', '1', 1, 0, 3, 0, 0);


INSERT INTO sys_role_menu (role_id, menu_id)
VALUES ('0', '1'),
       ('0', '2'),
       ('0', '2-1'),
       ('0', '2-2'),
       ('0', '2-3');


INSERT INTO sys_menu_permission (menu_id, "key", value)
VALUES ('2-2', 'sys:menu:add', '新增'),
       ('2-2', 'sys:menu:delete', '删除'),
       ('2-2', 'sys:menu:edit', '编辑'),
       ('2-2', 'sys:menu:list', '列表'),
       ('2-3', 'sys:role:add', '新增'),
       ('2-3', 'sys:role:delete', '删除'),
       ('2-3', 'sys:role:edit', '编辑'),
       ('2-3', 'sys:role:list', '列表'),
       ('2-1', 'sys:user:add', '新增'),
       ('2-1', 'sys:user:delete', '删除'),
       ('2-1', 'sys:user:edit', '编辑'),
       ('2-1', 'sys:user:list', '列表');


INSERT INTO sys_role_permission (role_id, permission_key)
VALUES ('0', 'sys:menu:add'),
       ('0', 'sys:menu:delete'),
       ('0', 'sys:menu:edit'),
       ('0', 'sys:menu:list'),
       ('0', 'sys:role:add'),
       ('0', 'sys:role:delete'),
       ('0', 'sys:role:edit'),
       ('0', 'sys:role:list'),
       ('0', 'sys:user:add'),
       ('0', 'sys:user:delete'),
       ('0', 'sys:user:edit'),
       ('0', 'sys:user:list');

