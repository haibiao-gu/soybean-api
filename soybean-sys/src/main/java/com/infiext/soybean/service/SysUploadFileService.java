package com.infiext.soybean.service;

import com.infiext.soybean.domain.SortParam;
import com.infiext.soybean.po.SysUploadFilePO;
import com.mybatisflex.core.paginate.Page;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysUploadFileService {
    @Transactional
    SysUploadFilePO uploadFile(MultipartFile file, String bizType, String bizId);

    void downloadFile(String fileId, HttpServletResponse response);

    @Transactional
    SysUploadFilePO create(SysUploadFilePO po);

    @Transactional
    void createBatch(List<SysUploadFilePO> list);

    @Transactional
    SysUploadFilePO update(SysUploadFilePO po);

    @Transactional
    void deleteByIds(List<String> ids);

    SysUploadFilePO getById(String id);

    Page<SysUploadFilePO> getPage(SysUploadFilePO query, Page<SysUploadFilePO> page, SortParam sort);

    List<SysUploadFilePO> getList(SysUploadFilePO query, SortParam sort);
}
