package com.infiext.soybean.utils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 排序重置服务
 */
@Service
public class SortResetService {

    /**
     * 重置排序顺序
     *
     * @param ids             ID 列表
     * @param entitySupplier  实体供应器
     * @param queryBuilder    查询构建器
     * @param idExtractor     ID 提取器
     * @param sortOrderSetter 排序顺序设置器
     * @param updateAction    更新操作
     * @param <T>             实体类型
     */
    @Transactional
    public <T> void resetSortOrder(
            List<String> ids,
            Supplier<T> entitySupplier,
            Function<List<String>, List<T>> queryBuilder,
            Function<T, String> idExtractor,
            BiConsumer<T, Integer> sortOrderSetter,
            Runnable updateAction
    ) {
        // 原数据列表
        List<T> originList = queryBuilder.apply(ids);

        // 根据 ids 的顺序对 originList 进行重新排序，并更新 sortOrder 不一致的数据
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            // 在 originList 中查找对应的实体
            T entity = originList.stream()
                    .filter(po -> idExtractor.apply(po).equals(id))
                    .findFirst()
                    .orElse(null);

            if (entity != null) {
                int expectedSortOrder = i + 1;

                // 检查并更新 sortOrder
                sortOrderSetter.accept(entity, expectedSortOrder);

                // 执行更新操作（如果需要）
                if (updateAction != null) {
                    updateAction.run();
                }
            }
        }
    }
}