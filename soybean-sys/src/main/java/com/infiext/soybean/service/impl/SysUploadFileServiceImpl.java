package com.infiext.soybean.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.enums.FileStatusEnum;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.mapper.SysUploadFileMapper;
import com.infiext.soybean.po.SysUploadFilePO;
import com.infiext.soybean.service.SysUploadFileService;
import com.infiext.soybean.upload.enums.FileStoreType;
import com.infiext.soybean.upload.model.DownloadStoreRequest;
import com.infiext.soybean.upload.model.UploadStoreRequest;
import com.infiext.soybean.upload.model.UploadStoreResult;
import com.infiext.soybean.upload.service.UploadStoreFactory;
import com.infiext.soybean.utils.FileUtils;
import com.infiext.soybean.utils.SortUtil;
import com.infiext.soybean.validator.sys.file.SysUploadFileValidationContext;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.infiext.soybean.po.table.SysUploadFileTableDef.SYS_UPLOAD_FILE;

@Service
public class SysUploadFileServiceImpl implements SysUploadFileService {
    @Resource
    private SysUploadFileValidationContext validator;
    @Resource
    private SysUploadFileMapper mapper;
    @Resource
    private UploadStoreFactory uploadStoreFactory;

    @Value("${app.default.upload-store}")
    private String defaultUploadStore;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件信息
     */
    @Transactional
    @Override
    public SysUploadFilePO uploadFile(MultipartFile file, String bizType, String bizId) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        try {
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

            SysUploadFilePO filePO = SysUploadFilePO.create();
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

            return filePO;
        } catch (IOException e) {
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
        SysUploadFilePO filePO = getById(fileId);
        if (filePO == null) {
            throw new BusinessException("文件不存在");
        }
        FileStoreType storeType = filePO.getStoreType() == null ? FileStoreType.LOCAL : filePO.getStoreType();
        DownloadStoreRequest request = DownloadStoreRequest.builder()
                .filePath(filePO.getFilePath())
                .originalFileName(filePO.getOriginalFileName())
                .mimeType(filePO.getMimeType())
                .build();
        uploadStoreFactory.get(storeType).download(request, response);
    }

    /**
     * 创建
     */
    @Transactional
    @Override
    public SysUploadFilePO create(SysUploadFilePO po) {
        validator.validateAll(po);
        po.save();
        return po;
    }

    /**
     * 批量创建
     */
    @Transactional
    @Override
    public void createBatch(List<SysUploadFilePO> list) {
        list.forEach(this::create);
    }

    /**
     * 更新
     */
    @Transactional
    @Override
    public SysUploadFilePO update(SysUploadFilePO po) {
        validator.validateAll(po);
        boolean status = po.updateById();
        if (!status) {
            throw new BusinessException("修改失败，数据已被他人更新！");
        }
        return po;
    }

    /**
     * 逻辑删除
     */
    @Transactional
    @Override
    public void deleteByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) return;
        mapper.deleteBatchByIds(ids);
    }

    /**
     * 获取
     */
    @Override
    public SysUploadFilePO getById(String id) {
        return SysUploadFilePO.create().setId(id).withRelations().oneById();
    }

    /**
     * 获取分页
     */
    @Override
    public Page<SysUploadFilePO> getPage(SysUploadFilePO query, Page<SysUploadFilePO> page, SortParam sort) {
        QueryWrapper queryWrapper = getQueryWrapper(query, sort);
        return mapper.paginateWithRelations(page, queryWrapper);
    }

    /**
     * 获取列表
     */
    @Override
    public List<SysUploadFilePO> getList(SysUploadFilePO query, SortParam sort) {
        QueryWrapper queryWrapper = getQueryWrapper(query, sort);
        return mapper.selectListWithRelationsByQuery(queryWrapper);
    }

    /**
     * 获取查询条件
     */
    private QueryWrapper getQueryWrapper(SysUploadFilePO query, SortParam sort) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(SYS_UPLOAD_FILE.DEFAULT_COLUMNS);
        return SortUtil.orderBy(queryWrapper, sort, SysUploadFilePO.class, SYS_UPLOAD_FILE.CREATE_TIME.asc());
    }

    private FileStoreType resolveUploadStoreType() {
        FileStoreType storeType = FileStoreType.from(defaultUploadStore);
        if (storeType == null) {
            throw new BusinessException("默认上传存储类型不支持：" + defaultUploadStore);
        }
        return storeType;
    }

}