package com.infiext.soybean.service.impl;

import com.infiext.soybean.enums.ConfigGroupEnum;
import com.infiext.soybean.po.SysConfigPO;
import com.infiext.soybean.service.SysConfigService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * SysConfigService 存根实现 — 在未配置数据库时作为默认 Bean 兜底。
 * <p>
 * 当数据库就绪后，可替换为真正的数据库实现（如 SysConfigServiceImpl），
 * 届时本存根需标注 @ConditionalOnMissingBean 或直接删除。
 */
@Service
public class SysConfigServiceStubImpl implements SysConfigService {

    @Override
    public List<SysConfigPO> getByGroup(ConfigGroupEnum group) {
        return Collections.emptyList();
    }

    @Override
    public void saveBatch(List<SysConfigPO> configs) {
        // 存根不做持久化
    }

    @Override
    public String getConfigValue(ConfigGroupEnum group, String key) {
        // 所有配置项均返回 null，调用方会按"未配置"处理（抛出 BusinessException 提示用户配置）
        return null;
    }

    @Override
    public boolean testMail(String to) {
        return false;
    }

    @Override
    public boolean testUpload() {
        return false;
    }
}
