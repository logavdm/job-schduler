package com.loga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.loga"})
public class SpringBootSchdulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSchdulerApplication.class, args);
	}

}
