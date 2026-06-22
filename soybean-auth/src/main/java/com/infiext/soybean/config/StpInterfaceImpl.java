package com.infiext.soybean.config;

import cn.dev33.satoken.stp.StpInterface;
import com.infiext.soybean.service.AuthService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private AuthService authService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return authService.getPermissionList(loginId.toString(), loginType);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return authService.getRoleList(loginId.toString(), loginType);
    }
}
