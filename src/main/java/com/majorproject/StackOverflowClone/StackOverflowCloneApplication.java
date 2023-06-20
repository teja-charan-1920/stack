package com.majorproject.StackOverflowClone;

import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StackOverflowCloneApplication {
	public static void main(String[] args) {
		SpringApplication.run(StackOverflowCloneApplication.class, args);
	}

}
