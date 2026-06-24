package com.infiext.soybean.service;

import com.infiext.soybean.enums.FileStoreType;
import com.infiext.soybean.model.DownloadStoreRequest;
import com.infiext.soybean.model.UploadStoreRequest;
import com.infiext.soybean.model.UploadStoreResult;
import jakarta.servlet.http.HttpServletResponse;

public interface UploadStoreService {
    FileStoreType getStoreType();

    UploadStoreResult upload(UploadStoreRequest request);

    void download(DownloadStoreRequest request, HttpServletResponse response);
}
