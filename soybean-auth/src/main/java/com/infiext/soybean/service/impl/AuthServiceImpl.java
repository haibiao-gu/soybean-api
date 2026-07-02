package com.infiext.soybean.service.impl;

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
     */
    @Override
    public List<String> getPermissionList(String userId, String loginType) {
        return sysUserService.getPermissionList(userId);
    }

    /**
     * 获取角色列表
     */
    @Override
    public List<String> getRoleList(String userId, String loginType) {
        return sysUserService.getRoleList(userId);
    }

    /**
     * 登录
     *
     * @param username     用户名（手机号码）
     * @param passwordHash SHA-256(password + salt) 前端已加盐哈希的结果
     * @return 用户 ID
     */
    @Override
    public String login(String username, String passwordHash) {
        // passwordHash is already SHA-256(password + salt), BCrypt compare directly in getUserId
        String userId = sysUserService.getUserId(username, passwordHash);
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
     * 获取用户盐值
     *
     * @param userName 用户名（手机号码）
     * @return 盐值
     */
    @Override
    public String getSalt(String userName) {
        SysUserPO user = sysUserService.getByPhone(userName);
        if (user == null) {
            throw new BusinessException("用户不存在！");
        }
        return user.getSalt();
    }

    /**
     * 修改密码
     *
     * @param userId        用户ID
     * @param oldPasswordHash SHA-256(oldPassword + salt)，前端已加盐
     * @param newPasswordHash SHA-256(newPassword + salt)，前端已加盐
     */
    @Override
    public void changePassword(String userId, String oldPasswordHash, String newPasswordHash) {
        SysUserPO sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw new BusinessException("用户不存在！");
        }
        // oldPasswordHash is SHA-256(oldPassword + salt), BCrypt compare directly
        if (!passwordEncoder.matches(oldPasswordHash, sysUser.getPassword())) {
            throw new BusinessException("原密码错误！");
        }
        // newPasswordHash is SHA-256(newPassword + salt), BCrypt encode and store
        sysUserService.updatePassword(userId, newPasswordHash);
    }
}
