package com.example.timetable_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
		org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
public class TimetableServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetableServiceApplication.class, args);
	}

}
