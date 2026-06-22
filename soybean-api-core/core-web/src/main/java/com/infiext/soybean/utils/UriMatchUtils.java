package com.infiext.soybean.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * URI 匹配工具类：判断当前请求是否需要排除处理
 */
public class UriMatchUtils {
    // Spring 内置的 Ant 风格路径匹配器（支持/**通配符）
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public static boolean isExcludeUri(HttpServletRequest request, List<String> excludeUris) {
        if (request == null || CollectionUtils.isEmpty(excludeUris)) {
            return false;
        }

        if (CollectionUtils.isEmpty(excludeUris)) {
            return false;
        }

        // 1. 获取应用上下文路径（如/api），无配置则为空字符串
        String contextPath = request.getContextPath();
        // 2. 获取请求的绝对URI（如/api/health）
        String requestUri = request.getRequestURI();
        // 3. 剥离上下文路径，得到相对URI（如/health）
        String relativeUri = getRelativeUri(requestUri, contextPath);

        // 4. 用相对URI匹配排除列表
        for (String excludeUri : excludeUris) {
            if (PATH_MATCHER.match(excludeUri, relativeUri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 剥离上下文路径，得到相对 URI
     *
     * @param requestUri  绝对 URI（如/api/health）
     * @param contextPath 上下文路径（如/api）
     * @return 相对URI（如/health）
     */
    private static String getRelativeUri(String requestUri, String contextPath) {
        // 无上下文路径，直接返回原 URI
        if (contextPath == null || contextPath.isEmpty() || "/".equals(contextPath)) {
            return requestUri;
        }
        // 剥离前缀（确保 URI 以上下文路径开头）
        if (requestUri.startsWith(contextPath)) {
            String relativeUri = requestUri.substring(contextPath.length());
            // 处理剥离后为空的情况（如请求 URI 是/api，剥离后为空，返回/）
            return relativeUri.isEmpty() ? "/" : relativeUri;
        }
        // 特殊情况：URI 不以上下文路径开头（理论上不会出现），返回原URI
        return requestUri;
    }

}