package com.infiext.soybean.service.impl;

import cn.hutool.core.io.FileUtil;
import com.infiext.soybean.enums.ConfigGroupEnum;
import com.infiext.soybean.enums.FileStoreType;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.model.DownloadStoreRequest;
import com.infiext.soybean.model.UploadStoreRequest;
import com.infiext.soybean.model.UploadStoreResult;
import com.infiext.soybean.service.SysConfigService;
import com.infiext.soybean.service.UploadStoreService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class LocalUploadStoreService implements UploadStoreService {
    @Resource
    private SysConfigService sysConfigService;

    @Override
    public FileStoreType getStoreType() {
        return FileStoreType.LOCAL;
    }

    @Override
    public UploadStoreResult upload(UploadStoreRequest request) {
        String localUploadDir = sysConfigService.getConfigValue(ConfigGroupEnum.UPLOAD, "local_dir");
        if (!StringUtils.hasText(localUploadDir)) {
            throw new BusinessException("请先配置本地上传目录");
        }
        String fullPath = localUploadDir + File.separator + request.getRelativePath();
        File destFile = new File(fullPath);
        File parentDir = destFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        FileUtil.writeBytes(request.getFileBytes(), destFile);

        return UploadStoreResult.builder()
                .storeType(FileStoreType.LOCAL)
                .bucketName(localUploadDir)
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
