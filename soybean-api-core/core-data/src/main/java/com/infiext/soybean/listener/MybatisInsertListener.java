package com.infiext.soybean.listener;

import cn.dev33.satoken.stp.StpUtil;
import com.infiext.soybean.domain.BasePO;
import com.mybatisflex.annotation.InsertListener;

import java.time.LocalDateTime;

public class MybatisInsertListener implements InsertListener {
    @Override
    public void onInsert(Object o) {
        if (o instanceof BasePO) {
            ((BasePO<?>) o).setCreateTime(LocalDateTime.now());
            try {
                if (StpUtil.isLogin()) {
                    String userId = StpUtil.getLoginIdAsString();
                    ((BasePO<?>) o).setCreateBy(userId);
                }
            } catch (Exception ignored) {
            }
        }
    }
}