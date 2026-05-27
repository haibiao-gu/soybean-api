package com.infiext.soybean.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.infiext.soybean.enums.DelEnum;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.activerecord.Model;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BasePO<T extends Model<T>> extends Model<T> {
    /**
     * 主键
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    private String id;
    /**
     * 创建人
     */
    @Column(value = "create_by")
    private String createBy;
    /**
     * 创建时间
     */
    @Column(value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 更新人
     */
    @Column(value = "update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @Column(value = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    /**
     * 删除标识
     */
    @Column(value = "del_flag", isLogicDelete = true)
    private DelEnum delFlag;
    /**
     * 版本号
     */
    @Column(value = "version", version = true)
    private Integer version;

    public static <T extends Model<T>> BasePO<T> create() {
        return new BasePO<>();
    }
}
