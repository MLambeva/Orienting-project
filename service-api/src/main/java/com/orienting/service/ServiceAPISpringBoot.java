package com.orienting.service;

import com.orienting.common.entity.UserEntity;
import com.orienting.common.entity.UserRole;
import com.orienting.common.services.AuthenticationService;
import com.orienting.common.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.orienting.service.configuration",
        "com.orienting.service.controller",
        "com.orienting.service.test"
})

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
