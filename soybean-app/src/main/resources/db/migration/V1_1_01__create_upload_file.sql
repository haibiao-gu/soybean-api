DROP TABLE IF EXISTS upload_file;
CREATE TABLE upload_file(
                                id VARCHAR(32) NOT NULL,
                                create_by VARCHAR(32),
                                create_time TIMESTAMP,
                                update_by VARCHAR(32),
                                update_time TIMESTAMP,
                                del_flag INT4 NOT NULL,
                                version INT4 NOT NULL,
                                biz_type VARCHAR(50) NOT NULL,
                                biz_id VARCHAR(32) NOT NULL,
                                original_file_name VARCHAR(500),
                                file_name VARCHAR(50),
                                file_suffix VARCHAR(50),
                                mime_type VARCHAR(500) NOT NULL,
                                file_size VARCHAR(500),
                                file_md5 VARCHAR(32) NOT NULL,
                                store_type VARCHAR(50),
                                bucket_name VARCHAR(50),
                                file_key VARCHAR(500),
                                file_path VARCHAR(500),
                                file_url VARCHAR(500),
                                status VARCHAR(10),
                                PRIMARY KEY (id)
);
COMMENT ON COLUMN upload_file.id IS '主键';
COMMENT ON COLUMN upload_file.create_by IS '创建人';
COMMENT ON COLUMN upload_file.create_time IS '创建时间';
COMMENT ON COLUMN upload_file.update_by IS '更新人';
COMMENT ON COLUMN upload_file.update_time IS '更新时间';
COMMENT ON COLUMN upload_file.del_flag IS '删除标识';
COMMENT ON COLUMN upload_file.version IS '版本号';
COMMENT ON COLUMN upload_file.biz_type IS '业务模块';
COMMENT ON COLUMN upload_file.biz_id IS '业务表主键';
COMMENT ON COLUMN upload_file.original_file_name IS '原文件名';
COMMENT ON COLUMN upload_file.file_name IS '文件名称';
COMMENT ON COLUMN upload_file.file_suffix IS '文件后缀';
COMMENT ON COLUMN upload_file.mime_type IS 'MIME 类型';
COMMENT ON COLUMN upload_file.file_size IS '文件大小';
COMMENT ON COLUMN upload_file.file_md5 IS '文件哈希';
COMMENT ON COLUMN upload_file.store_type IS '存储位置（1-本地,2-MinIO）';
COMMENT ON COLUMN upload_file.bucket_name IS '桶名';
COMMENT ON COLUMN upload_file.file_key IS '存储系统中的唯一 key';
COMMENT ON COLUMN upload_file.file_path IS '文件路径';
COMMENT ON COLUMN upload_file.file_url IS '文件URL';
COMMENT ON COLUMN upload_file.status IS '状态（0-上传中,1-完成,2-失败）';
COMMENT ON TABLE upload_file IS '上传文件管理';
