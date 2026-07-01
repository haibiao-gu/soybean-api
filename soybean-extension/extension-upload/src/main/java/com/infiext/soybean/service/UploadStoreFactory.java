package com.infiext.soybean.service;

import com.infiext.soybean.enums.FileStoreType;
import com.infiext.soybean.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class UploadStoreFactory {
    private final Map<FileStoreType, UploadStoreService> storeServiceMap = new EnumMap<>(FileStoreType.class);

    public UploadStoreFactory(List<UploadStoreService> storeServices) {
        for (UploadStoreService storeService : storeServices) {
            UploadStoreService previous = storeServiceMap.put(storeService.getStoreType(), storeService);
            if (previous != null) {
                throw new IllegalStateException("重复的文件存储实现：" + storeService.getStoreType());
            }
        }
    }

    public UploadStoreService get(FileStoreType storeType) {
        UploadStoreService storeService = storeServiceMap.get(storeType);
        if (storeService == null) {
            throw new BusinessException("未找到文件存储实现：" + storeType);
        }
        return storeService;
    }
}
