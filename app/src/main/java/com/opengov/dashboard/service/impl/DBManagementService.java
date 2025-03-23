package com.opengov.dashboard.service.impl;

import com.opengov.dashboard.service.interfaces.SchemaManagementService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
@Transactional
@Slf4j
public class DBManagementService {
    @Autowired
    SchemaManagementService schemaManagementService;

    @Autowired
    @Qualifier("adminHikariDataSource")
    DataSource dataSource;

    @PostConstruct
    public void initSchema() {
        log.info("Creating schema...");
        schemaManagementService.applyChangelog(dataSource,
                "public",
                "changelog/healthcaredb.sql");
    }
}
