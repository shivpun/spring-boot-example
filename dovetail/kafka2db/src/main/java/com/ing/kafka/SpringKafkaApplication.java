package com.ing.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@ComponentScan(basePackages= {"com.ing.kafka.config"})
public class SpringKafkaApplication {
	
	public static void main(String []args) {
		SpringApplication.run(SpringKafkaApplication.class, args);
	}
}
