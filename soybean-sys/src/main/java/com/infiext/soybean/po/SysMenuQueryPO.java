package com.infiext.soybean.po;

import com.infiext.soybean.utils.excel.annotation.ExcelField;
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
    @ExcelField(value = "菜单ID", unique = false, sort = 0, required = true, maxLength = 32)
    private String menuId;

    /**
     * 参数名
     */
    @Column(value = "key")
    @ExcelField(value = "参数名", unique = false, sort = 1, required = true, maxLength = 64)
    private String key;

    /**
     * 参数值
     */
    @Column(value = "value")
    @ExcelField(value = "参数值", unique = false, sort = 2, required = true, maxLength = 64)
    private String value;

}