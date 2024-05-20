package com.example.demo2;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
class Demo2ApplicationTests {
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest");

	@BeforeAll
	static void databaseOn() {
		POSTGRES.start();
	}

	@AfterAll
	static void databaseOff() {
		POSTGRES.stop();
	}

	@Test
	void contextLoads() {
	}

}
