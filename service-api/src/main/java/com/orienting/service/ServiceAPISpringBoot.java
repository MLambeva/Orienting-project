package com.orienting.service;

import com.orienting.service.entity.UserEntity;
import com.orienting.service.entity.UserRole;
import com.orienting.service.services.AuthenticationService;
import com.orienting.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Properties;

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
    public void run(String... args) throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));

        String email = properties.getProperty("api.auth.email");
        String password = properties.getProperty("api.auth.password");
        String name = properties.getProperty("api.auth.name");
        String ucn = properties.getProperty("api.auth.ucn");

        UserEntity admin = new UserEntity(email, password, name, name, ucn, UserRole.ADMIN);
        if(userService.getUsers().isEmpty()) authenticationService.register(admin);
    }
}
