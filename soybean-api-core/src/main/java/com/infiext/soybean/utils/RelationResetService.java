package com.infiext.soybean.utils;

import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Service
public class RelationResetService {

    /**
     * 重置关联关系（支持值转换）
     *
     * @param id          主实体 ID
     * @param values      关联值列表
     * @param handler     关系处理器
     * @param valueSetter 值设置器
     * @param <T>         关联实体类型
     * @param <V>         关联值类型
     */
    @Transactional
    public <T, V> void resetRelations(String id, List<V> values, RelationResetHandler<T> handler, BiConsumer<V, T> valueSetter) {
        // 删除旧的关联关系
        QueryWrapper deleteQuery = handler.buildDeleteQuery(id);
        handler.getMapper().deleteByQuery(deleteQuery);

        // 如果没有新的关联关系，直接返回
        if (values == null || values.isEmpty()) {
            return;
        }

        // 创建实体并设置值
        List<T> pos = new ArrayList<>();
        for (V value : values) {
            try {
                T po = handler.getEntitySupplier().get();
                handler.setForeignKey(po, id);
                valueSetter.accept(value, po); // 设置具体值
                pos.add(po);
            } catch (Exception e) {
                // 记录日志或抛出异常，根据业务需求处理
                throw new RuntimeException("无法创建实体实例", e);
            }
        }

        handler.getMapper().insertBatch(pos);
    }

    /**
     * 重置关联关系
     *
     * @param id        主实体 ID
     * @param relations 关联实体列表
     * @param handler   关系处理器
     * @param <T>       关联实体类型
     */
    @Transactional
    public <T> void resetRelations(String id, List<T> relations, RelationResetHandler<T> handler) {
        // 删除旧的关联关系
        QueryWrapper deleteQuery = handler.buildDeleteQuery(id);
        handler.getMapper().deleteByQuery(deleteQuery);

        // 如果没有新的关联关系，直接返回
        if (relations == null || relations.isEmpty()) {
            return;
        }

        // 设置外键并批量插入
        relations.forEach(relation -> handler.setForeignKey(relation, id));
        handler.getMapper().insertBatch(relations);
    }
}
