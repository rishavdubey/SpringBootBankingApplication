package com.example.userapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@ComponentScan({"com.example.userapplication.repository","com.example.userapplication.service","com.example.userapplication.controller"})
//@ComponentScan("com.example.userapplication.repository")
//@EnableJpaRepositories(basePackages = "com.example.userapplication.repository")
//@EntityScan(basePackages = "com.example.userapplication.entity")
//@ComponentScan({"com.example.userapplication.service","com.example.userapplication.controller"})
//@EnableAutoConfiguration
@EnableFeignClients
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
