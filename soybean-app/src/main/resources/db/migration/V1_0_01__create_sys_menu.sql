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

INSERT INTO sys_menu (id, del_flag, "version", status, parent_id, menu_type, menu_name, route_name, route_path, component, icon, icon_type, keep_alive, constant, sort_order, hide_in_menu, multi_tab)
VALUES ('1', 0, 0, '1', '0', '2', '首页', 'home', '/home', 'layout.base$view.home', 'mdi:monitor-dashboard', '1', 1, 0, 1, 0, 0),
       ('2', 0, 0, '1', '0', '1', '系统设定', 'system', '/system', 'layout.base', 'carbon:cloud-service-management', '1', 0, 0, 5, 0, 0),
       ('2-2', 0, 0, '1', '2', '2', '菜单管理', 'system_menu', '/system/menu', 'view.system_menu', 'material-symbols:route', '1', 1, 0, 2, 0, 0);
