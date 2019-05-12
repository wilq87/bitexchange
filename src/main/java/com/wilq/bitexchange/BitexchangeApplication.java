package com.wilq.bitexchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BitexchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BitexchangeApplication.class, args);
	}

}
