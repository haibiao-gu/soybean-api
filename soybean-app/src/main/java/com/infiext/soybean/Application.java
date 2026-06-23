package com.infiext.soybean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.Map;

@Slf4j
@SpringBootApplication
public class Application {
    private static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationRunner printServerInfo(Environment environment) {
        return args -> {
            // 基础信息
            String springBootVersion = SpringBootVersion.getVersion();
            String javaVersion = System.getProperty("java.version");
            String javaVersionStatus = "21".equals(javaVersion.split("\\.")[0])
                    ? "✅ Java 21（支持虚拟线程）"
                    : "⚠️ 非Java 21环境（当前：" + javaVersion + "）";
            String osName = System.getProperty("os.name");
            String osArch = System.getProperty("os.arch");
            String appName = environment.getProperty("spring.application.name", "未配置应用名称");
            String appVersion = environment.getProperty("app.version", "1.0.0");
            String activeProfile = String.join(",", environment.getActiveProfiles());
            activeProfile = activeProfile.isEmpty() ? "默认环境（dev）" : activeProfile;

            // 网络信息
            String port = environment.getProperty("server.port", "8080");
            String contextPath = environment.getProperty("server.servlet.context-path", "");
            contextPath = contextPath.isEmpty() ? "/" : contextPath;
            String hostAddress;
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                hostAddress = "127.0.0.1";
                // 异常单独warn输出（不混入主信息块）
                log.warn("⚠️ 获取本机IP失败，使用默认IP: 127.0.0.1", e);
            }
            String accessUrl = String.format("http://%s:%s%s", hostAddress, port, contextPath);

            // JVM & Java 21特性
            Runtime runtime = Runtime.getRuntime();
            Map<String, Long> jvmInfo = Map.of(
                    "maxMemory", runtime.maxMemory() / 1024 / 1024,
                    "totalMemory", runtime.totalMemory() / 1024 / 1024,
                    "freeMemory", runtime.freeMemory() / 1024 / 1024
            );
            String virtualThreadSupport = isVirtualThreadSupported() ? "✅ 支持" : "❌ 不支持";

            long endTime = System.currentTimeMillis();
            long startupTimeMs = endTime - startTime; // 耗时（毫秒）
            double startupTimeS = startupTimeMs / 1000.0; // 耗时（秒，保留1位小数）
            String startupTimeDesc = String.format("%d 毫秒（%.1f 秒）", startupTimeMs, startupTimeS);
            String uploadInfoBlock = buildUploadInfoBlock(environment);

            String fullServerInfo = """
                    
                    ==================================================
                    ✅ 服务端启动成功 [%s - v%s]
                    ==================================================
                    📌 基础信息
                    --------------------------------------------------
                    SpringBoot版本：%s
                    Java版本：%s
                    操作系统：%s (%s)
                    运行环境：%s
                    启动耗时：%s
                    访问地址：%s
                    
                    📌 JVM & Java 21特性
                    --------------------------------------------------
                    最大堆内存：%d MB
                    当前堆内存：%d MB
                    空闲堆内存：%d MB
                    虚拟线程支持：%s
                    %s
                    ==================================================
                    🎉 应用已就绪，可正常访问！
                    ==================================================
                    """.formatted(
                    appName, appVersion,
                    springBootVersion,
                    javaVersionStatus,
                    osName, osArch,
                    activeProfile,
                    startupTimeDesc,
                    accessUrl,
                    jvmInfo.get("maxMemory"),
                    jvmInfo.get("totalMemory"),
                    jvmInfo.get("freeMemory"),
                    virtualThreadSupport,
                    uploadInfoBlock
            );
            log.info(fullServerInfo);
        };
    }

    /**
     * 检测Java 21虚拟线程支持
     */
    private boolean isVirtualThreadSupported() {
        try {
            Class.forName("java.lang.Thread$Builder$OfVirtual");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private String buildUploadInfoBlock(Environment environment) {
        String uploadStore = firstNonBlank(
                environment.getProperty("app.default.upload-store"),
                environment.getProperty("app.upload-store")
        );
        if (!StringUtils.hasText(uploadStore)) {
            return "";
        }

        String storeType = uploadStore.trim().toUpperCase(Locale.ROOT);
        if ("LOCAL".equals(storeType)) {
            String localDir = firstNonBlank(
                    environment.getProperty("app.default.local-upload-dir"),
                    environment.getProperty("app.local-upload-dir")
            );
            if (!StringUtils.hasText(localDir)) {
                return "";
            }
            return """
                    
                    📌 文件上传配置
                    --------------------------------------------------
                    存储方式：LOCAL
                    本地上传目录：%s
                    """.formatted(localDir);
        }

        if ("MINIO".equals(storeType)) {
            String endpoint = environment.getProperty("app.minio.endpoint");
            String bucketName = environment.getProperty("app.minio.bucket-name");
            String publicUrl = environment.getProperty("app.minio.public-url");
            if (!StringUtils.hasText(endpoint) || !StringUtils.hasText(bucketName)) {
                return "";
            }
            String displayPublicUrl = StringUtils.hasText(publicUrl) ? publicUrl : "未配置";
            return """
                    
                    📌 文件上传配置
                    --------------------------------------------------
                    存储方式：MINIO
                    MinIO Endpoint：%s
                    MinIO Bucket：%s
                    MinIO Public URL：%s
                    """.formatted(endpoint, bucketName, displayPublicUrl);
        }

        return "";
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }
}