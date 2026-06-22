package com.infiext.soybean.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求排除 URI 配置
 */
@Component
@ConfigurationProperties(prefix = "request.exclude")
public class LogExcludeConfig {

    private List<String> uris = new ArrayList<>();

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }
}

