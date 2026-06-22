package com.infiext.soybean.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 菜单路由查询参数 数据表的PO对象
 */
@Table("sys_menu_query")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysMenuQueryPO extends Model<SysMenuQueryPO> {
    /**
     * 菜单ID
     */
    @Column(value = "menu_id")
    private String menuId;

    /**
     * 参数名
     */
    @Column(value = "key")
    private String key;

    /**
     * 参数值
     */
    @Column(value = "value")
    private String value;

}