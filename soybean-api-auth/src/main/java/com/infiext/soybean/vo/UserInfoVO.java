package com.infiext.soybean.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoVO {
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
    /**
     * 权限列表
     */
    private List<String> permissions;
}
