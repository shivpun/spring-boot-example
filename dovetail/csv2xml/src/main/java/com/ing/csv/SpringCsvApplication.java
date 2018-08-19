package com.ing.csv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.ing"})
public class SpringCsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCsvApplication.class, args);
	}

}
