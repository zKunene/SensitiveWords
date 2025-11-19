package com.sensitivewords.assessment;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.MSSQLServerContainer;

@TestConfiguration
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	MSSQLServerContainer<?> mssqlContainer() {
		return new MSSQLServerContainer<>(
			DockerImageName.parse("mcr.microsoft.com/mssql/server:2022-latest")
			).acceptLicense();
	}
}
