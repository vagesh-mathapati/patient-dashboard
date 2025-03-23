package com.opengov.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.opengov.dashboard.jpa.repository")
public class PatientDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientDashboardApplication.class, args);
	}

}
