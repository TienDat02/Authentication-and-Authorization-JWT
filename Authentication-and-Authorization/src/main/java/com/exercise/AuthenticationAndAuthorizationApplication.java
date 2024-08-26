package com.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class AuthenticationAndAuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationAndAuthorizationApplication.class, args);
	}

}
