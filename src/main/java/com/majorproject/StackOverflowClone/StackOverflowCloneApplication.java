package com.majorproject.StackOverflowClone;

import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StackOverflowCloneApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(StackOverflowCloneApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;
	@Override
	public void run(String... args) throws Exception {
		User user = new User();
		user.setEmail("a@gmail.com");
		user.setUsername("piyush");
		user.setPassword("1234");

		userRepository.save(user);
	}
}
