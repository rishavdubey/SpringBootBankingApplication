package com.example.config_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.io.File;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {
	private static final Logger logger= LoggerFactory.getLogger(ConfigServerApplication.class);


	public static void main(String[] args) {


		try{
			String currDir= System.getProperty("user.dir");
			logger.info("curr Dir :: {} ", currDir);
			String environment= System.getenv("environment");
			logger.info("Environment :: {}",environment);
			System.setProperty("environment",environment);
			System.setProperty("spring.config.location",currDir + "/Config/application.properties");
		}catch (Exception e){
			logger.error("Error");
		}


		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
