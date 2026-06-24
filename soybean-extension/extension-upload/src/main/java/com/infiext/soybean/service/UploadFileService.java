package com.infiext.soybean.service;

import com.infiext.soybean.po.UploadFilePO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    @Transactional
    UploadFilePO uploadFile(MultipartFile file, String bizType, String bizId);

    void downloadFile(String fileId, HttpServletResponse response);
}
