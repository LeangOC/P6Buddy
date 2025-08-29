package com.oc.P6Buddy;

import com.oc.P6Buddy.model.users;
import com.oc.P6Buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class P6BuddyApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;
	public static void main(String[] args) {
		SpringApplication.run(P6BuddyApplication.class, args);
	}
@Override
	public void run(String... args ) throws Exception {
	Iterable<users> users = userService.getUsers();
	users.forEach(user -> System.out.println(user.getUserName()));
	}
}
