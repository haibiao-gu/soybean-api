package com.infiext.soybean.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import com.infiext.soybean.enums.FileStatusEnum;
import com.infiext.soybean.enums.FileStoreType;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.model.DownloadStoreRequest;
import com.infiext.soybean.model.UploadStoreRequest;
import com.infiext.soybean.model.UploadStoreResult;
import com.infiext.soybean.po.UploadFilePO;
import com.infiext.soybean.service.UploadFileService;
import com.infiext.soybean.service.UploadStoreFactory;
import com.infiext.soybean.utils.FileUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Resource
    private UploadStoreFactory uploadStoreFactory;

    @Value("${app.upload-store}")
    private String uploadStore;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件信息
     */
    @Transactional
    @Override
    public UploadFilePO uploadFile(MultipartFile file, String bizType, String bizId) throws IOException {
        try {
            if (file == null || file.isEmpty()) {
                log.error("上传文件为空");
                throw new BusinessException("上传文件不能为空");
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new BusinessException("文件名不能为空");
            }

            long fileSize = file.getSize();
            String contentType = file.getContentType();

            String fileSuffix = FileUtil.extName(originalFileName);
            String fileName = IdUtil.fastSimpleUUID();

            byte[] fileBytes = file.getBytes();
            String fileMd5 = MD5.create().digestHex(fileBytes);

            String relativePath = FileUtils.buildFilePath(fileName);
            FileStoreType storeType = resolveUploadStoreType();
            UploadStoreRequest uploadRequest = UploadStoreRequest.builder()
                    .fileBytes(fileBytes)
                    .originalFileName(originalFileName)
                    .fileName(fileName)
                    .fileSuffix(fileSuffix)
                    .relativePath(relativePath)
                    .contentType(contentType)
                    .build();
            UploadStoreResult uploadResult = uploadStoreFactory.get(storeType).upload(uploadRequest);

            UploadFilePO filePO = UploadFilePO.create();
            filePO.setBizType(bizType);
            filePO.setBizId(bizId);
            filePO.setOriginalFileName(originalFileName);
            filePO.setFileName(fileName);
            filePO.setFileSuffix(fileSuffix);
            filePO.setMimeType(contentType);
            filePO.setFileSize(FileUtils.formatFileSize(fileSize));
            filePO.setFileMd5(fileMd5);
            filePO.setStoreType(uploadResult.getStoreType());
            filePO.setBucketName(uploadResult.getBucketName());
            filePO.setFileKey(uploadResult.getFileKey());
            filePO.setFilePath(uploadResult.getFilePath());
            filePO.setFileUrl(uploadResult.getFileUrl());
            filePO.setStatus(FileStatusEnum.COMPLETED);

            filePO.save();

            log.info("文件 {} 上传成功，ID: {}", originalFileName, filePO.getId());
            return filePO;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 下载文件
     *
     * @param fileId   文件ID
     * @param response HTTP响应对象
     */
    @Override
    public void downloadFile(String fileId, HttpServletResponse response) {
        UploadFilePO filePO = UploadFilePO.create().setId(fileId).oneById();
        if (filePO == null) {
            throw new BusinessException("文件不存在");
        }
        FileStoreType storeType = filePO.getStoreType() == null ? FileStoreType.LOCAL : filePO.getStoreType();
        DownloadStoreRequest request = DownloadStoreRequest.builder()
                .bucketName(filePO.getBucketName())
                .fileKey(filePO.getFileKey())
                .filePath(filePO.getFilePath())
                .originalFileName(filePO.getOriginalFileName())
                .mimeType(filePO.getMimeType())
                .build();
        uploadStoreFactory.get(storeType).download(request, response);
    }

    private FileStoreType resolveUploadStoreType() {
        FileStoreType storeType = FileStoreType.from(uploadStore);
        if (storeType == null) {
            throw new BusinessException("默认上传存储类型不支持：" + uploadStore);
        }
        return storeType;
    }

}