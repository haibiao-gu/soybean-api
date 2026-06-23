DROP TABLE IF EXISTS sys_menu_query;
CREATE TABLE sys_menu_query(
                               menu_id VARCHAR(32) NOT NULL,
                               key VARCHAR(64) NOT NULL,
                               value VARCHAR(64) NOT NULL
);
COMMENT ON COLUMN sys_menu_query.menu_id IS '菜单ID';
COMMENT ON COLUMN sys_menu_query.key IS '参数名';
COMMENT ON COLUMN sys_menu_query.value IS '参数值';
COMMENT ON TABLE sys_menu_query IS '菜单路由查询参数';
