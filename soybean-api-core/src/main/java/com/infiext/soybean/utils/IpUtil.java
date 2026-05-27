package com.infiext.soybean.utils;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * IP 地址工具
 */
public class IpUtil {

    /**
     * 获取请求方 IP 地址
     *
     * @return IP 地址
     */
    public static String getClientIp() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        return getClientIp(request);
    }

    private static boolean getClientIp(String ip) {
        return !StrUtil.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 获取客户端真实IP（处理反向代理/负载均衡场景）
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个IP时取第一个（X-Forwarded-For可能包含多个IP，用逗号分隔）
        return ip != null && ip.contains(",") ? ip.split(",")[0].trim() : ip;
    }

}
