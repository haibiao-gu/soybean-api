package com.infiext.soybean.po;

import com.infiext.soybean.domain.BasePO;
import com.infiext.soybean.enums.ConfigGroupEnum;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Table("sys_config")
@Accessors(chain = true)
@Data(staticConstructor = "create")
@EqualsAndHashCode(callSuper = true)
public class SysConfigPO extends BasePO<SysConfigPO> {
    @Column(value = "config_group")
    private ConfigGroupEnum configGroup;

    @Column(value = "config_key")
    private String configKey;

    @Column(value = "config_value")
    private String configValue;

    @Column(value = "description")
    private String description;
}
