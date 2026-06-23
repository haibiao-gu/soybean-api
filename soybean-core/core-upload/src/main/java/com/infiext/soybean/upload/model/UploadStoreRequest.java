package com.infiext.soybean.upload.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadStoreRequest {
    private final byte[] fileBytes;
    private final String originalFileName;
    private final String fileName;
    private final String fileSuffix;
    private final String relativePath;
    private final String contentType;
}
