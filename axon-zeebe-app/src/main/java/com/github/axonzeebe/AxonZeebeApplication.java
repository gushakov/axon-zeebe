package com.github.axonzeebe;

import io.zeebe.spring.client.EnableZeebeClient;
import org.axonframework.springboot.autoconfig.JdbcAutoConfiguration;
import org.axonframework.springboot.autoconfig.JpaEventStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class,
		JpaEventStoreAutoConfiguration.class, JdbcAutoConfiguration.class})
@EnableZeebeClient
public class AxonZeebeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AxonZeebeApplication.class, args);
	}

}