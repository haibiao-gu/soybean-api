package com.infiext.soybean.service.impl;

import com.infiext.soybean.config.SecurityConfig;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.po.SysUserPO;
import com.infiext.soybean.service.AuthService;
import com.infiext.soybean.service.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Resource
    private SysUserService sysUserService;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 获取权限列表
     *
     * @param userId    用户 ID
     * @param loginType 登录类型
     * @return 权限列表
     */
    @Override
    public List<String> getPermissionList(String userId, String loginType) {
        return sysUserService.getPermissionList(userId);
    }

    /**
     * 获取角色列表
     *
     * @param userId    用户 ID
     * @param loginType 登录类型
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(String userId, String loginType) {
        return sysUserService.getRoleList(userId);
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户 ID
     */
    @Override
    public String login(String username, String password) {
        String userId = sysUserService.getUserId(username, password);
        if (userId == null) {
            throw new BusinessException(400, "用户不存在或密码错误！");
        }
        return userId;
    }

    /**
     * 获取用户信息
     */
    @Override
    public SysUserPO getUserInfo(String userId) {
        return sysUserService.getById(userId);
    }


    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {
        SysUserPO sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw new BusinessException("用户不存在！");
        }
        // 新格式：SHA-256 后 BCrypt 比对
        String hashedOld = SecurityConfig.sha256(oldPassword);
        boolean matched = passwordEncoder.matches(hashedOld, sysUser.getPassword());
        // 兼容旧格式：直接 BCrypt 比对
        if (!matched) {
            matched = passwordEncoder.matches(oldPassword, sysUser.getPassword());
        }
        if (!matched) {
            throw new BusinessException("原密码错误！");
        }
        sysUserService.updatePassword(userId, newPassword);
    }
}
