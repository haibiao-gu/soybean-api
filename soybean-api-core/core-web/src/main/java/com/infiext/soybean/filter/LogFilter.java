package com.infiext.soybean.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infiext.soybean.config.LogExcludeConfig;
import com.infiext.soybean.utils.IpUtil;
import com.infiext.soybean.utils.UriMatchUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 请求/响应日志过滤器：记录请求 URL、参数、IP、响应状态、响应体等
 */
@Slf4j
@Component
@WebFilter(urlPatterns = "/*")
@Order(1) // 保证过滤器优先执行
public class LogFilter implements Filter {
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    // 注入排除 URI 配置
    private final LogExcludeConfig logExcludeConfig;
    private final ObjectMapper objectMapper;

    // 构造器注入
    public LogFilter(LogExcludeConfig logExcludeConfig, ObjectMapper objectMapper) {
        this.logExcludeConfig = logExcludeConfig;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // ========== 核心：判断是否排除当前URI ==========
        if (UriMatchUtils.isExcludeUri(httpRequest, logExcludeConfig.getUris())) {
            // 排除的URI：直接放行，不记录日志
            chain.doFilter(request, response);
            return;
        }

        // 包装请求/响应
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // 记录请求开始时间 & 获取基础请求信息
        long startTime = System.currentTimeMillis();
        String requestUrl = httpRequest.getRequestURL().toString();
        String method = httpRequest.getMethod();
        String clientIp = IpUtil.getClientIp(httpRequest);
        Map<String, String> headers = getRequestHeaders(httpRequest);
        String queryString = httpRequest.getQueryString();

        // 执行后续过滤器/Controller
        chain.doFilter(requestWrapper, responseWrapper);

        String requestBody = getRequestBody(requestWrapper);
        String singleLineBody = formatSingleLineBody(requestBody);

        // 获取响应信息
        int statusCode = responseWrapper.getStatus();
        String responseBody = getResponseBody(responseWrapper);
        long costTime = System.currentTimeMillis() - startTime;

        // ========== 拼接日志 ==========
        String logContent = """
                
                ============ 请求/响应日志 ============
                请求URL: %s
                请求方法: %s
                客户端IP: %s
                请求头: %s
                GET参数: %s
                POST请求体: %s
                响应状态码: %d
                响应体: %s
                请求耗时: %dms
                ====================================
                """.formatted(
                requestUrl,
                method,
                clientIp,
                headers,
                (queryString == null ? "无" : queryString),
                (singleLineBody.isEmpty() ? "无" : singleLineBody),
                statusCode,
                responseBody,
                costTime
        );
        log.info(logContent);

        // 写回响应体
        responseWrapper.copyBodyToResponse();
    }

    /**
     * 获取POST请求体（GET请求体为空）
     */
    private String getRequestBody(ContentCachingRequestWrapper requestWrapper) {
        try {
            // 从缓存中获取字节数组（ContentCachingRequestWrapper已缓存）
            byte[] content = requestWrapper.getContentAsByteArray();
            if (content.length == 0) {
                return "";
            }
            // 关键：使用请求的编码（默认UTF-8，兼容自定义编码）
            String charset = requestWrapper.getCharacterEncoding();
            if (charset.isEmpty()) {
                charset = StandardCharsets.UTF_8.name();
            }
            return new String(content, charset);
        } catch (Exception e) {
            log.error("读取 POST 请求体失败", e);
            return "读取请求体失败：" + e.getMessage();
        }
    }

    /**
     * 核心工具方法：将请求体格式化为单行（去除换行、多余空格）
     *
     * @param body 原始请求体（可能包含换行、空格）
     * @return 单行紧凑的请求体
     */
    private String formatSingleLineBody(String body) {
        if (body == null || body.isEmpty()) {
            return "";
        }
        // 尝试按JSON紧凑化处理，失败则按普通字符串处理
        try {
            Object jsonObj = objectMapper.readValue(body, Object.class);
            return objectMapper.writeValueAsString(jsonObj); // 无空格的紧凑JSON
        } catch (JsonProcessingException e) {
            // 非JSON格式，按原有逻辑处理
            String singleLine = WHITESPACE_PATTERN.matcher(body).replaceAll(" ");
            return singleLine.trim();
        }
    }

    /**
     * 获取响应体
     */
    private String getResponseBody(ContentCachingResponseWrapper responseWrapper) {
        byte[] content = responseWrapper.getContentAsByteArray();
        return content.length == 0 ? "" : new String(content, StandardCharsets.UTF_8);
    }

    /**
     * 获取请求头信息
     */
    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        return headers;
    }
}