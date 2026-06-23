package com.infiext.soybean.upload.service.impl;

import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.upload.enums.FileStoreType;
import com.infiext.soybean.upload.model.DownloadStoreRequest;
import com.infiext.soybean.upload.model.UploadStoreRequest;
import com.infiext.soybean.upload.model.UploadStoreResult;
import com.infiext.soybean.upload.service.UploadStoreService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class MinioUploadStoreService implements UploadStoreService {
    @Value("${app.minio.endpoint}")
    private String endpoint;
    @Value("${app.minio.access-key}")
    private String accessKey;
    @Value("${app.minio.secret-key}")
    private String secretKey;
    @Value("${app.minio.bucket-name}")
    private String bucketName;
    @Value("${app.minio.public-url}")
    private String publicUrl;

    @Override
    public FileStoreType getStoreType() {
        return FileStoreType.MINIO;
    }

    @Override
    public UploadStoreResult upload(UploadStoreRequest request) {
        String validatedBucketName = resolveBucketName(null);
        String objectKey = request.getRelativePath().replace("\\", "/");
        MinioClient minioClient = buildClient();
        try {
            ensureBucket(minioClient, validatedBucketName);
            String contentType = StringUtils.hasText(request.getContentType()) ? request.getContentType() : "application/octet-stream";
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(validatedBucketName)
                    .object(objectKey)
                    .stream(new ByteArrayInputStream(request.getFileBytes()), request.getFileBytes().length, -1)
                    .contentType(contentType)
                    .build());
            return UploadStoreResult.builder()
                    .storeType(FileStoreType.MINIO)
                    .bucketName(validatedBucketName)
                    .fileKey(objectKey)
                    .filePath(buildObjectUrl(validatedBucketName, objectKey))
                    .fileUrl(buildObjectUrl(validatedBucketName, objectKey))
                    .build();
        } catch (Exception e) {
            throw new BusinessException("MinIO上传失败：" + e.getMessage());
        }
    }

    @Override
    public void download(DownloadStoreRequest request, HttpServletResponse response) {
        MinioClient minioClient = buildClient();
        String validatedBucketName = resolveBucketName(request.getBucketName());
        String objectKey = request.getFileKey();
        if (!StringUtils.hasText(objectKey)) {
            throw new BusinessException("MinIO文件Key不能为空");
        }
        try (GetObjectResponse objectResponse = minioClient.getObject(GetObjectArgs.builder()
                .bucket(validatedBucketName)
                .object(objectKey)
                .build());
             OutputStream outputStream = response.getOutputStream()) {
            String encodedFileName = URLEncoder.encode(request.getOriginalFileName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            String mimeType = StringUtils.hasText(request.getMimeType()) ? request.getMimeType() : "application/octet-stream";
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodedFileName);
            objectResponse.transferTo(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            throw new BusinessException("MinIO文件下载失败：" + e.getMessage());
        } catch (Exception e) {
            throw new BusinessException("MinIO文件下载失败：" + e.getMessage());
        }
    }

    private MinioClient buildClient() {
        if (!StringUtils.hasText(endpoint) || !StringUtils.hasText(accessKey) || !StringUtils.hasText(secretKey)) {
            throw new BusinessException("MinIO配置不完整，请检查 endpoint/access-key/secret-key");
        }
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    private void ensureBucket(MinioClient minioClient, String validatedBucketName) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(validatedBucketName)
                .build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(validatedBucketName)
                    .build());
        }
    }

    private String resolveBucketName(String requestBucketName) {
        String value = StringUtils.hasText(requestBucketName) ? requestBucketName : bucketName;
        if (!StringUtils.hasText(value)) {
            throw new BusinessException("MinIO桶名称不能为空");
        }
        return value;
    }

    private String buildObjectUrl(String validatedBucketName, String objectKey) {
        String base = StringUtils.hasText(publicUrl) ? publicUrl : endpoint;
        if (!StringUtils.hasText(base)) {
            return "/" + validatedBucketName + "/" + objectKey;
        }
        String normalizedBase = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
        return normalizedBase + "/" + validatedBucketName + "/" + objectKey;
    }
}
