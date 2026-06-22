package com.infiext.soybean.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private Long expire;
}
