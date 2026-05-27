package com.infiext.soybean.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 请求排除配置：映射application.yml中的排除URI列表
 */
@Data
@Component
@ConfigurationProperties(prefix = "request.exclude")
public class LogExcludeConfig {
    /**
     * 排除的URI列表（支持精确匹配、前缀匹配/**）
     */
    private List<String> uris;
}