package com.infiext.soybean.listener;

import cn.dev33.satoken.stp.StpUtil;
import com.infiext.soybean.domain.BasePO;
import com.mybatisflex.annotation.UpdateListener;

import java.time.LocalDateTime;

public class MybatisUpdateListener implements UpdateListener {
    @Override
    public void onUpdate(Object o) {
        if (o instanceof BasePO) {
            ((BasePO<?>) o).setUpdateTime(LocalDateTime.now());
            try {
                if (StpUtil.isLogin()) {
                    String userId = StpUtil.getLoginIdAsString();
                    ((BasePO<?>) o).setUpdateBy(userId);
                }
            } catch (Exception ignored) {
            }
        }
    }
}