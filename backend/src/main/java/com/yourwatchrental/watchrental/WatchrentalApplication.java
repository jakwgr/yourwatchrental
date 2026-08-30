package com.yourwatchrental.watchrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WatchrentalApplication {

	static void main(String[] args) {
		SpringApplication.run(WatchrentalApplication.class, args);
	}

}
