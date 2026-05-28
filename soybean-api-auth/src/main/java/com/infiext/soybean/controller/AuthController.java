package com.infiext.soybean.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.infiext.soybean.dto.ChangePasswordDTO;
import com.infiext.soybean.dto.LoginDTO;
import com.infiext.soybean.po.SysUserPO;
import com.infiext.soybean.service.AuthService;
import com.infiext.soybean.vo.LoginVO;
import com.infiext.soybean.vo.UserInfoVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public LoginVO loginVO(@Validated @RequestBody LoginDTO dto) {
        String userId = authService.login(dto.getUserName(), dto.getPassword());
        StpUtil.login(userId);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        LoginVO vo = new LoginVO();
        vo.setToken(tokenInfo.getTokenValue());
        vo.setExpire(tokenInfo.getTokenTimeout());
        return vo;
    }

    @SaCheckLogin
    @PostMapping("/getUserInfo")
    public UserInfoVO getUserInfo() {
        String userId = StpUtil.getLoginIdAsString();
        SysUserPO po = authService.getUserInfo(userId);
        List<String> permissions = authService.getPermissionList(userId, null);
        UserInfoVO vo = new UserInfoVO();
        vo.setAvatar(po.getUserAvatar());
        vo.setNickname(po.getUserName());
        vo.setPhone(po.getUserPhone());
        vo.setPermissions(permissions);
        return vo;
    }

    @SaCheckLogin
    @PostMapping("/getPermissions")
    public List<String> getPermissions(@RequestParam(required = false) String loginType) {
        String userId = StpUtil.getLoginIdAsString();
        return authService.getPermissionList(userId, loginType);
    }

    @SaCheckLogin
    @PostMapping("/getRoles")
    public List<String> getRoles(@RequestParam String loginType) {
        String userId = StpUtil.getLoginIdAsString();
        return authService.getRoleList(userId, loginType);
    }
    
    @SaCheckLogin
    @PostMapping("/changePassword")
    public void changePassword(@RequestBody ChangePasswordDTO dto) {
        String userId = StpUtil.getLoginIdAsString();
        authService.changePassword(userId, dto.getOldPassword(), dto.getNewPassword());
    }
    
}
