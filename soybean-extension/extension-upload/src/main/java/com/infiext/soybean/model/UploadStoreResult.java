package com.infiext.soybean.model;

import com.infiext.soybean.enums.FileStoreType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadStoreResult {
    private final FileStoreType storeType;
    private final String bucketName;
    private final String fileKey;
    private final String filePath;
    private final String fileUrl;
}
