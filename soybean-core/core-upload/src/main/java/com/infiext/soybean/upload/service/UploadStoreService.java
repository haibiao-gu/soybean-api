package com.infiext.soybean.upload.service;

import com.infiext.soybean.upload.enums.FileStoreType;
import com.infiext.soybean.upload.model.DownloadStoreRequest;
import com.infiext.soybean.upload.model.UploadStoreRequest;
import com.infiext.soybean.upload.model.UploadStoreResult;
import jakarta.servlet.http.HttpServletResponse;

public interface UploadStoreService {
    FileStoreType getStoreType();

    UploadStoreResult upload(UploadStoreRequest request);

    void download(DownloadStoreRequest request, HttpServletResponse response);
}
