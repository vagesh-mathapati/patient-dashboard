package com.opengov.dashboard.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

@Configuration
@Slf4j
@RefreshScope
@EnableJpaRepositories(basePackages = "com.opengov.dashboard.repository")
@EnableTransactionManagement
public class DataSourceConfiguration {
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH = "hibernate.max_fetch_depth";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    @Value("${spring.datasource.hikari.jdbcUrl}")
    private String url;

    @Value("${spring.datasource.hikari.username}")
    private String username;

    @Value("${spring.datasource.hikari.password}")
    private String password;
    
    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.hikari.maximumPoolSize}")
    private int maxPoolSize;

    @Value("${spring.datasource.hikari.minimumIdle}")
    private int minimumIdle;

    @Value("${spring.datasource.hikari.poolName}")
    private String poolName;

    @Autowired
    private Environment env;

    @Bean("adminHikariDataSource")
    public HikariDataSource adminHikariDataSource() {
        final int idleTimeOut = 30000;
        final int connectionTimeOut = 30000;
        final int maxLifeTime = 2000000;

        log.info("username : {}", username);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(getPassword());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.setMaximumPoolSize(maxPoolSize);
        config.setIdleTimeout(idleTimeOut);
        config.setPoolName(poolName);
        config.setConnectionTimeout(connectionTimeOut);
        config.setMaxLifetime(maxLifeTime);
        config.setMinimumIdle(minimumIdle);
        config.setDriverClassName(driver);

        return new HikariDataSource(config);
    }

    /**
     * @return password returns value read from environment variable or from mounted file
     */
    private String getPassword() {
        if (StringUtils.isEmpty(password)) {
            // TODO read password file name and path from env
            Path path = Paths.get("/var/secrets/postgresdbpassword.txt");
            try {
                List<String> lines = Files.readAllLines(path);
                if (!lines.isEmpty()) {
                    password = lines.get(0);
                }
            } catch (IOException e) {
                log.error("Failed to read password file", e);
                throw new RuntimeException(
                        "Set \"spring.datasource.hikari.password\" env or make sure file \"/var/secrets/postgresdbpassword.txt\" is available");
            }
        }
        return password;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdaptor());
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
         entityManagerFactoryBean.setJpaProperties(jpaHibernateProperties());
        // Set the package that contains @Entity classes
        entityManagerFactoryBean.setPackagesToScan("com.opengov.dashboard.jpa");

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return transactionManager;
    }
    private HibernateJpaVendorAdapter vendorAdaptor() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(false);
        return vendorAdapter;
    }

    private Properties jpaHibernateProperties() {
        // TODO setup properties
        Properties properties = new Properties();

        // properties.put(PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH, env.getProperty(PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH));
        // properties.put(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE, env.getProperty(PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE));
        // properties.put(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE, env.getProperty(PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE));
        // properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));

        // properties.put(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, "none");
        // properties.put(AvailableSettings.USE_CLASS_ENHANCER, "false");
        return properties;
    }

}
