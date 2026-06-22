package com.infiext.soybean.domain;

import com.infiext.soybean.enums.OrderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortParam {
    private String columnKey;
    private OrderEnum order;
}
