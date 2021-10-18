package com.example.BlogAppApi;

import com.example.BlogAppApi.models.RoleModel;
import com.example.BlogAppApi.models.UserModel;
import com.example.BlogAppApi.service.UserService;
import com.example.BlogAppApi.service.UserServiceImplementation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class BlogAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApiApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserServiceImplementation userServiceImplementation){
		return args -> {
			UserModel user = new UserModel("henock","1234","henock@gmail.com");
			userServiceImplementation.saveUser(user);
			RoleModel role = new RoleModel(null,"ROLE_USER");
			userServiceImplementation.saveRole(role);
			userServiceImplementation.addRoleToUser(user.getUserName(),role.getName());
			System.out.println(userServiceImplementation.getUser(user.getId()));
		};
	}

}
