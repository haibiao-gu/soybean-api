package com.infiext.soybean.vo;

import lombok.Data;

@Data
public class UserInfoVO {
    private String userId;
    /**
     * 用户姓名
     */
    private String nickname;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户头像
     */
    private String avatar;
}
