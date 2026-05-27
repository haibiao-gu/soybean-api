package com.infiext.soybean.utils;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.query.QueryWrapper;

import java.util.function.Supplier;

/**
 * 关系重置处理器接口
 *
 * @param <T> 关联实体类型
 */
public interface RelationResetHandler<T> {
    /**
     * 获取关联实体的 Mapper
     *
     * @return BaseMapper
     */
    BaseMapper<T> getMapper();

    /**
     * 获取实体供应器
     *
     * @return Supplier
     */
    Supplier<T> getEntitySupplier();

    /**
     * 构建删除查询条件
     *
     * @param id 实体 ID
     * @return QueryWrapper
     */
    QueryWrapper buildDeleteQuery(String id);

    /**
     * 设置关联实体的外键值
     *
     * @param po 关联实体
     * @param id 主实体 ID
     */
    void setForeignKey(T po, String id);
}
