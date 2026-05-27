package com.infiext.soybean.handler;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.enums.OrderEnum;
import com.mybatisflex.core.paginate.Page;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 参数处理工具类
 */
public class ParamHandler {

    /**
     * 私有构造函数，防止实例化工具类
     */
    private ParamHandler() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }

    /**
     * 构建排序参数
     */
    public static SortParam buildSortParam(String sortBy, String order) {
        OrderEnum orderEnum = StringUtils.hasText(order)
                ? OrderEnum.valueOf(order.toLowerCase())
                : OrderEnum.none;
        return new SortParam(sortBy, orderEnum);
    }

    /**
     * 构建分页对象
     */
    public static <T> Page<T> buildPage(Integer pageNumber, Integer pageSize) {
        return Page.of(pageNumber != null ? pageNumber : 1,
                pageSize != null ? pageSize : 10);
    }

    /**
     * 将空 List 转换为 null（用于查询参数预处理）
     *
     * @param list 输入列表
     * @return 如果为空列表返回 null，否则返回原列表
     */
    public static <T> List<T> emptyListToNull(List<T> list) {
        return (list != null && list.isEmpty()) ? null : list;
    }
}