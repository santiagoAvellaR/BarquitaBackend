package edu.eci.cvds.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableMongoRepositories
public class TaskApplication {

	public static void main(String[] args) {

		SpringApplication.run(TaskApplication.class, args);
	}

}
