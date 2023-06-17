package com.developer.smallRoom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SmallRoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallRoomApplication.class, args);
	}

}
