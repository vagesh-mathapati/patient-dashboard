package com.opengov.dashboard.service.impl;

import com.opengov.dashboard.service.interfaces.SchemaManagementService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.exception.LockException;
import liquibase.lockservice.DatabaseChangeLogLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

@Service
@Slf4j
public class LiquibaseService implements SchemaManagementService {

    @Value("${spring.application.name:#{null}}")
    private String serviceName;

    @Value("${application.name:#{null}}")
    private String appName;

    private final LiquibaseObjectHelper liquibaseObjectHelper = new LiquibaseObjectHelper();

    public void applyChangelog(DataSource dataSource, String schemaName, String changeLogFile) {
        if (dataSource != null) {
            try {
                log.debug("Applying liquibase updates for schema {} ", schemaName);

                try (final Connection connection = dataSource.getConnection()) {
                    connection.setSchema(schemaName);
                    final Database database = DatabaseFactory.getInstance()
                            .findCorrectDatabaseImplementation(new JdbcConnection(connection));
                    try (final Liquibase liquibase = liquibaseObjectHelper.getLiquiBaseInstance(changeLogFile, database)) {
                        applyUpdate(liquibase, schemaName);
                    }
                }
            } catch (Exception ex) {
                log.error("Schema {} Exception occurred while applying changelog ", schemaName, ex);
            }
        }
    }

    private void applyUpdate(@NonNull Liquibase liquibase, @NonNull String schemaName) throws LiquibaseException {
        try {
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LockException lockException) {
            log.error("Schema {} Lock Exception in applyChangelog", schemaName, lockException);
            processLockException(liquibase);
        }
    }

    private void processLockException(Liquibase liquibase) {
        try {
            log.info("Lock Exception occurred Retrying liquibase updates by releasing lock serviceName {} appName {}",
                    serviceName, appName);

            final DatabaseChangeLogLock[] arrlocks = liquibase.listLocks();
            if (arrlocks != null && arrlocks.length > 0) {
                final DatabaseChangeLogLock changeLogLock = arrlocks[0];
                log.info("Currently liquibase lock hold by {}", changeLogLock.getLockedBy());

                if (changeLogLock.getLockedBy() != null && !changeLogLock.getLockedBy().isEmpty()) {
                    checkAndReleaseLockHeldByNonRunningPod(liquibase, changeLogLock);
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while applying changelog in LockException ", e);
        }
    }

    private void checkAndReleaseLockHeldByNonRunningPod(
            @NonNull Liquibase liquibase, @NonNull DatabaseChangeLogLock changeLogLock
    ) throws LiquibaseException {
        try (final KubernetesClient client = liquibaseObjectHelper.getKubernetesClient()) {
            final List<Pod> servicePods = client.pods()
                    .inNamespace(client.getNamespace())
                    .withField("status.phase", "Running")
                    .list()
                    .getItems();
            if (servicePods != null && !servicePods.isEmpty()) {
                final boolean isLockedByRunningPod = isLockedByRunningPod(changeLogLock, servicePods);
                log.info("isLockedByRunningPod {}", isLockedByRunningPod);
                if (!isLockedByRunningPod) {
                    liquibase.forceReleaseLocks();
                    liquibase.update(new Contexts(), new LabelExpression());
                }
            }
        }
    }

    private boolean isLockedByRunningPod(DatabaseChangeLogLock changeLogLock, List<Pod> servicePods) {
        return servicePods.stream()
                .anyMatch(
                        pod -> pod.getMetadata().getName().contains(serviceName != null ? serviceName : appName)
                                && changeLogLock.getLockedBy().contains(pod.getMetadata().getName()));
    }
}
