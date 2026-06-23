package com.infiext.soybean.upload.service.impl;

import cn.hutool.core.io.FileUtil;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.upload.enums.FileStoreType;
import com.infiext.soybean.upload.model.DownloadStoreRequest;
import com.infiext.soybean.upload.model.UploadStoreRequest;
import com.infiext.soybean.upload.model.UploadStoreResult;
import com.infiext.soybean.upload.service.UploadStoreService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
public class LocalUploadStoreService implements UploadStoreService {
    @Value("${app.default.bucket-name}")
    private String defaultBucketName;

    @Override
    public FileStoreType getStoreType() {
        return FileStoreType.LOCAL;
    }

    @Override
    public UploadStoreResult upload(UploadStoreRequest request) {
        String fullPath = defaultBucketName + File.separator + request.getRelativePath();
        File destFile = new File(fullPath);
        File parentDir = destFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        FileUtil.writeBytes(request.getFileBytes(), destFile);

        return UploadStoreResult.builder()
                .storeType(FileStoreType.LOCAL)
                .bucketName(defaultBucketName)
                .filePath(fullPath)
                .fileUrl("/api/file/" + request.getRelativePath().replace(File.separator, "/"))
                .build();
    }

    @Override
    public void download(DownloadStoreRequest request, HttpServletResponse response) {
        String filePath = request.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new BusinessException("文件物理路径不存在：" + filePath);
        }

        try {
            String encodedFileName = URLEncoder.encode(request.getOriginalFileName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            response.setContentType(request.getMimeType());
            response.setContentLengthLong(file.length());
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodedFileName);

            try (OutputStream outputStream = response.getOutputStream()) {
                Files.copy(file.toPath(), outputStream);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new BusinessException("文件下载失败：" + e.getMessage());
        }
    }
}
