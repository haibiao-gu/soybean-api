package com.infiext.soybean.config;

import com.infiext.soybean.domain.BasePO;
import com.infiext.soybean.listener.MybatisInsertListener;
import com.infiext.soybean.listener.MybatisUpdateListener;
import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.AuditMessage;
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MybatisFlexConfigure implements MyBatisFlexCustomizer {

    @Override
    public void customize(FlexGlobalConfig globalConfig) {
        // 原构造方法中的监听器注册
        MybatisInsertListener insertListener = new MybatisInsertListener();
        MybatisUpdateListener updateListener = new MybatisUpdateListener();
        globalConfig.registerInsertListener(insertListener, BasePO.class);
        globalConfig.registerUpdateListener(updateListener, BasePO.class);
        //开启审计功能
        AuditManager.setAuditEnable(true);
        AuditManager.setMessageCollector(this::writeSqlLog);
    }

    /**
     * 打印 SQL 语句
     */
    private void writeSqlLog(AuditMessage auditMessage) {
        log.info("SQL：耗时[{}ms] {}",
                auditMessage.getElapsedTime(),
                auditMessage.getFullSql());
    }

}