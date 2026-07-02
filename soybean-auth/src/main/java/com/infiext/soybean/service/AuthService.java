package com.infiext.soybean.service;

import com.infiext.soybean.po.SysUserPO;

import java.util.List;

public interface AuthService {
    List<String> getPermissionList(String userId, String loginType);

    List<String> getRoleList(String userId, String loginType);

    String login(String username, String passwordHash);

    SysUserPO getUserInfo(String userId);

    String getSalt(String userName);

    void changePassword(String userId, String oldPasswordHash, String newPasswordHash);
}
