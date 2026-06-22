package com.infiext.soybean.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infiext.soybean.config.LogExcludeConfig;
import com.infiext.soybean.domain.Result;
import com.infiext.soybean.utils.UriMatchUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一响应体包装：将所有接口的返回值自动包装成 {code, data, msg} 格式
 */
@ControllerAdvice // 全局 Controller 增强
public class ResponseResultAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;
    private final LogExcludeConfig logExcludeConfig;

    // 构造器注入配置
    public ResponseResultAdvice(ObjectMapper objectMapper, LogExcludeConfig logExcludeConfig) {
        this.objectMapper = objectMapper;
        this.logExcludeConfig = logExcludeConfig;
    }

    /**
     * 判断是否需要包装响应：返回true表示所有接口都需要包装
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 1. 排除已经是 Result 类型的返回值
        if (returnType.getParameterType().isAssignableFrom(Result.class)) {
            return false;
        }

        // 2. 获取当前请求，判断是否在排除 URI 列表中
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return true; // 无请求上下文，默认包装
        }
        HttpServletRequest request = attributes.getRequest();
        return !UriMatchUtils.isExcludeUri(request, logExcludeConfig.getUris());
    }

    /**
     * 包装响应体的核心方法
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String bodyStr) {
            Result<String> result = Result.success(bodyStr);
            return serializeResult(result);
        }
        return Result.success(body);
    }


    /**
     * 封装 JSON 序列化工具方法，统一处理异常
     */
    private String serializeResult(Result<?> result) {
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            // 序列化失败时返回兜底 JSON
            return "{\"code\":500,\"data\":null,\"msg\":\"响应序列化失败：" + e.getMessage() + "\"}";
        }
    }
}