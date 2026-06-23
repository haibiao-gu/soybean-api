package com.infiext.soybean.po;

import com.infiext.soybean.domain.BasePO;
import com.infiext.soybean.enums.FileStatusEnum;
import com.infiext.soybean.upload.enums.FileStoreType;
import com.infiext.soybean.utils.excel.annotation.ExcelField;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 上传文件管理 数据表的PO对象
 */
@Table("sys_upload_file")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysUploadFilePO extends BasePO<SysUploadFilePO> {
    /**
     * 业务模块
     */
    @Column(value = "biz_type")
    @ExcelField(value = "业务模块", unique = false, sort = 7, required = true, maxLength = 50)
    private String bizType;

    /**
     * 业务表主键
     */
    @Column(value = "biz_id")
    @ExcelField(value = "业务表主键", unique = false, sort = 8, required = true, maxLength = 32)
    private String bizId;

    /**
     * 原文件名
     */
    @Column(value = "original_file_name")
    @ExcelField(value = "原文件名", unique = false, sort = 9, required = false, maxLength = 500)
    private String originalFileName;

    /**
     * 文件名称
     */
    @Column(value = "file_name")
    @ExcelField(value = "文件名称", unique = false, sort = 10, required = false, maxLength = 50)
    private String fileName;

    /**
     * 文件后缀
     */
    @Column(value = "file_suffix")
    @ExcelField(value = "文件后缀", unique = false, sort = 11, required = false, maxLength = 50)
    private String fileSuffix;

    /**
     * MIME 类型
     */
    @Column(value = "mime_type")
    @ExcelField(value = "MIME 类型", unique = false, sort = 12, required = true, maxLength = 500)
    private String mimeType;

    /**
     * 文件大小
     */
    @Column(value = "file_size")
    @ExcelField(value = "文件大小", unique = false, sort = 13, required = false, maxLength = 500)
    private String fileSize;

    /**
     * 文件哈希
     */
    @Column(value = "file_md5")
    @ExcelField(value = "文件哈希", unique = false, sort = 14, required = true, maxLength = 32)
    private String fileMd5;

    /**
     * 存储位置（1-本地,2-MinIO）
     */
    @Column(value = "store_type")
    @ExcelField(value = "存储位置（1-本地,2-MinIO）", unique = false, sort = 15, required = false, maxLength = 50)
    private FileStoreType storeType;

    /**
     * 桶名
     */
    @Column(value = "bucket_name")
    @ExcelField(value = "桶名", unique = false, sort = 16, required = false, maxLength = 50)
    private String bucketName;

    /**
     * 存储系统中的唯一 key
     */
    @Column(value = "file_key")
    @ExcelField(value = "存储系统中的唯一 key", unique = false, sort = 17, required = false, maxLength = 500)
    private String fileKey;

    /**
     * 文件路径
     */
    @Column(value = "file_path")
    @ExcelField(value = "文件路径", unique = false, sort = 18, required = false, maxLength = 500)
    private String filePath;

    /**
     * 文件URL
     */
    @Column(value = "file_url")
    @ExcelField(value = "文件URL", unique = false, sort = 19, required = false, maxLength = 500)
    private String fileUrl;

    /**
     * 状态（0-上传中,1-完成,2-失败）
     */
    @Column(value = "status")
    @ExcelField(value = "状态（0-上传中,1-完成,2-失败）", unique = false, sort = 20, required = false, maxLength = 10)
    private FileStatusEnum status;

}