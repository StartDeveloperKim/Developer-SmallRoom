package com.developer.smallRoom;

import com.developer.smallRoom.application.auth.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableConfigurationProperties(JwtProperties.class)
@SpringBootApplication
public class SmallRoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallRoomApplication.class, args);
	}

}
