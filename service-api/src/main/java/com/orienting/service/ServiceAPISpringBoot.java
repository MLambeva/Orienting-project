package com.orienting.service;

import com.orienting.service.entity.UserEntity;
import com.orienting.service.entity.UserRole;
import com.orienting.service.services.AuthenticationService;
import com.orienting.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceAPISpringBoot implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    public static void main(String[] args) {
        SpringApplication.run(ServiceAPISpringBoot.class, args);
    }

    @Override
    public void run(String... args) {
        UserEntity admin = new UserEntity("admin@abv.bg", "Password1", "Admin", "Admin", "8005203124", UserRole.ADMIN);
        if(userService.getUsers().isEmpty()) authenticationService.register(admin);
    }
}
