package com.infiext.soybean.service.impl;

import com.infiext.soybean.enums.ConfigGroupEnum;
import com.infiext.soybean.exception.BusinessException;
import com.infiext.soybean.po.SysConfigPO;
import com.infiext.soybean.service.SysConfigService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import static com.infiext.soybean.po.table.SysConfigTableDef.SYS_CONFIG;

@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Override
    public List<SysConfigPO> getByGroup(ConfigGroupEnum group) {
        return SysConfigPO.create().where(SYS_CONFIG.CONFIG_GROUP.eq(group)).list();
    }

    @Override
    @Transactional
    public void saveBatch(List<SysConfigPO> configs) {
        for (SysConfigPO config : configs) {
            SysConfigPO existing = SysConfigPO.create()
                    .where(SYS_CONFIG.CONFIG_GROUP.eq(config.getConfigGroup()))
                    .and(SYS_CONFIG.CONFIG_KEY.eq(config.getConfigKey()))
                    .one();
            if (existing != null) {
                existing.setConfigValue(config.getConfigValue());
                existing.setDescription(config.getDescription());
                existing.updateById();
            }
        }
    }

    @Override
    public String getConfigValue(ConfigGroupEnum group, String key) {
        SysConfigPO config = SysConfigPO.create()
                .where(SYS_CONFIG.CONFIG_GROUP.eq(group))
                .and(SYS_CONFIG.CONFIG_KEY.eq(key))
                .one();
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    public boolean testMail(String to) {
        String host = getConfigValue(ConfigGroupEnum.MAIL, "host");
        if (!StringUtils.hasText(host)) {
            throw new BusinessException("请先配置SMTP服务器地址");
        }
        try {
            JavaMailSenderImpl sender = buildMailSender();
            sender.testConnection();

            String from = getConfigValue(ConfigGroupEnum.MAIL, "from");
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("邮箱配置测试");
            helper.setText("这是一封来自Soybean系统的测试邮件，恭喜您邮箱配置成功！");
            sender.send(message);
            return true;
        } catch (Exception e) {
            throw new BusinessException("邮箱测试失败：" + e.getMessage());
        }
    }

    @Override
    public boolean testUpload() {
        String storeType = getConfigValue(ConfigGroupEnum.UPLOAD, "store_type");
        if (!StringUtils.hasText(storeType)) {
            throw new BusinessException("请先配置存储类型");
        }
        try {
            if ("MINIO".equalsIgnoreCase(storeType)) {
                String endpoint = getConfigValue(ConfigGroupEnum.UPLOAD, "minio_endpoint");
                String accessKey = getConfigValue(ConfigGroupEnum.UPLOAD, "minio_access_key");
                String secretKey = getConfigValue(ConfigGroupEnum.UPLOAD, "minio_secret_key");
                String bucketName = getConfigValue(ConfigGroupEnum.UPLOAD, "minio_bucket_name");
                if (!StringUtils.hasText(endpoint) || !StringUtils.hasText(accessKey)
                        || !StringUtils.hasText(secretKey) || !StringUtils.hasText(bucketName)) {
                    throw new BusinessException("MinIO配置不完整");
                }
                MinioClient client = MinioClient.builder()
                        .endpoint(endpoint)
                        .credentials(accessKey, secretKey)
                        .build();
                boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!exists) {
                    client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                }
            } else {
                String localDir = getConfigValue(ConfigGroupEnum.UPLOAD, "local_dir");
                if (!StringUtils.hasText(localDir)) {
                    throw new BusinessException("请先配置本地上传目录");
                }
                File dir = new File(localDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                if (!dir.isDirectory() || !dir.canWrite()) {
                    throw new BusinessException("本地目录不可写：" + localDir);
                }
            }
            return true;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("上传存储测试失败：" + e.getMessage());
        }
    }

    public JavaMailSenderImpl buildMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(getConfigValue(ConfigGroupEnum.MAIL, "host"));
        sender.setPort(Integer.parseInt(getConfigValue(ConfigGroupEnum.MAIL, "port")));
        sender.setUsername(getConfigValue(ConfigGroupEnum.MAIL, "username"));
        sender.setPassword(getConfigValue(ConfigGroupEnum.MAIL, "password"));
        sender.setProtocol("smtp");
        sender.setDefaultEncoding("UTF-8");

        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.auth", getConfigValue(ConfigGroupEnum.MAIL, "smtp_auth"));
        props.put("mail.smtp.starttls.enable", getConfigValue(ConfigGroupEnum.MAIL, "starttls_enable"));
        return sender;
    }
}
