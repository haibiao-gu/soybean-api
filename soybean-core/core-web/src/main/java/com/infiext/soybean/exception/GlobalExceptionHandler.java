package com.infiext.soybean.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.infiext.soybean.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：统一处理异常，返回标准响应格式
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 核心：处理自定义业务异常（优先级高于通用异常）
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        // 打印业务异常日志（级别为warn，区别于系统异常的error）
        log.warn("业务异常：code={}, msg={}", e.getCode(), e.getMessage(), e);
        // 返回自定义错误码和信息，适配统一响应格式
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数验证异常（@Valid/@Validated 校验失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 获取第一个字段错误信息（即最核心的校验失败原因）
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";

        // 打印详细日志（便于排查问题）
        log.warn("参数验证异常：{}", message, e);

        // 只返回简洁的错误提示（如"用户名不能为空"）
        return Result.error(400, message);
    }

    /**
     * 处理绑定异常（表单数据绑定校验失败）
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        // 获取第一个字段错误信息
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";

        // 打印详细日志
        log.warn("参数绑定异常：{}", message, e);

        // 返回简洁的错误提示
        return Result.error(400, message);
    }

    /**
     * 处理所有运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常：", e);
        return Result.error(500, "系统异常，请稍后重试");
    }

    /**
     * 处理所有未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("未知异常：", e);
        return Result.error(500, "系统异常，请稍后重试");
    }

    // ========== 新增：SA-Token 未登录异常处理 ==========
    @ExceptionHandler(NotLoginException.class)
    public Result<Void> handleNotLoginException(NotLoginException e) {
        // 获取未登录原因（如：token不存在、token过期、token被踢等）
        String loginType = e.getType();
        String msg = switch (loginType) {
            case NotLoginException.NOT_TOKEN -> "未登录：请求头中未携带token";
            case NotLoginException.INVALID_TOKEN -> "未登录：token无效/已篡改";
            case NotLoginException.TOKEN_TIMEOUT -> "未登录：token已过期";
            case NotLoginException.BE_REPLACED -> "未登录：账号已在其他设备登录";
            case NotLoginException.KICK_OUT -> "未登录：账号已被踢出";
            default -> "未登录：请先登录";
        };
        // 打印日志（含类名+行号+栈轨迹）
        log.warn("SA-Token未登录异常：msg={}, type={}", msg, loginType, e);
        // 返回401状态码（HTTP规范：401 Unauthorized）
        return Result.error(401, msg);
    }

    // ========== 新增：SA-Token 无权限异常处理 ==========
    @ExceptionHandler(NotPermissionException.class)
    public Result<Void> handleNotPermissionException(NotPermissionException e) {
        // 获取缺失的权限标识
        String lackPermission = e.getPermission();
        String msg = "无权限：缺少必要权限[" + lackPermission + "]，请联系管理员授权";
        // 打印日志（含类名+行号+栈轨迹）
        log.warn("SA-Token无权限异常：msg={}", msg, e);
        // 返回403状态码（HTTP规范：403 Forbidden）
        return Result.error(403, msg);
    }

    // ========== 新增：SA-Token 无角色异常处理 ==========
    @ExceptionHandler(NotRoleException.class)
    public Result<Void> handleNotRoleException(NotRoleException e) {
        // 获取缺失的角色标识
        String lackRole = e.getRole();
        String msg = "无角色：缺少必要角色[" + lackRole + "]，请联系管理员授权";
        // 打印日志（含类名+行号+栈轨迹）
        log.warn("SA-Token无角色异常：msg={}", msg, e);
        // 返回403状态码（HTTP规范：403 Forbidden）
        return Result.error(403, msg);
    }
}