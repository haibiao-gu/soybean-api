package com.infiext.soybean.upload.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DownloadStoreRequest {
    private final String bucketName;
    private final String fileKey;
    private final String filePath;
    private final String originalFileName;
    private final String mimeType;
}
