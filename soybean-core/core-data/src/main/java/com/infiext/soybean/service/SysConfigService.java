package com.infiext.soybean.service;

import com.infiext.soybean.enums.ConfigGroupEnum;
import com.infiext.soybean.po.SysConfigPO;

import java.util.List;

public interface SysConfigService {
    List<SysConfigPO> getByGroup(ConfigGroupEnum group);

    void saveBatch(List<SysConfigPO> configs);

    String getConfigValue(ConfigGroupEnum group, String key);

    boolean testMail(String to);

    boolean testUpload();
}
