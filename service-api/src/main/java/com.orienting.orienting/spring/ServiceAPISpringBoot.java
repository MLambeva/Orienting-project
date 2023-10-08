package com.orienting.orienting.spring;

import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.entity.UserRole;
import com.orienting.common.services.AuthenticationService;
import com.orienting.common.services.ClubService;
import com.orienting.common.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.orienting.orienting.spring.configuration",
        "com.orienting.orienting.spring.controller",
        "com.orienting.orienting.spring.test"
})

public class ServiceAPISpringBoot implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ClubService clubService;

    /*private static final List<ClubEntity> clubs = List.of(
            new ClubEntity("Bacho Kiro 94", "Dryanovo"),
            new ClubEntity("Uzana", "Gabrovo"),
            new ClubEntity("Brown Team", "Veliko Turnovo"),
            new ClubEntity("Valdi", "Sofia"),
            new ClubEntity("Istros", "Ruse"),
            new ClubEntity("Begun", "Varna")
    );

    private static final List<UserEntity> users = List.of(
            new UserEntity("admin@gmail.com", "Password", "Admin", "Admin", "8005203124", UserRole.ADMIN),
            new UserEntity("admin1@gmail.com", "Password", "Admin", "Admin", "8105203124", UserRole.ADMIN),
            new UserEntity("admin2@gmail.com", "Password", "Admin", "Admin", "8205203124", UserRole.ADMIN),
            new UserEntity("ivan_ivanov@abv.bg", "Password1", "Ivan", "Ivanov", "0042302211", "0879856542", "M35", UserRole.COACH, 1),
            new UserEntity("martina_hristova@abv.bg", "Password1", "Martina", "Hristova", "9042302211", "0899856542", "W21", UserRole.COACH, 1),
            new UserEntity("petur_petrov@abv.bg", "Password1", "Petur", "Petrov", "9402302211", "0889856542", "M21", UserRole.COMPETITOR, 1),
            new UserEntity("ivailo_ivanov@abv.bg", "Password1", "Ivailo", "Ivanov", "8002302211", "0888856542", "M40", UserRole.COMPETITOR, 1),
            new UserEntity("mariyan_ivanov@abv.bg", "Password1", "Mariyan", "Ivanov", "9602302211", "0878857543", "M21", UserRole.COMPETITOR, 1),
            new UserEntity("marina_petrova@abv.bg", "Password1", "Marina", "Petrova", "8402302211", "0878858543", "W35", UserRole.COACH, null),
            new UserEntity("monika_ivanova@abv.bg", "Password1", "Monika", "Ivanova", "9102302211", "0878887543", "W21", UserRole.COMPETITOR, null),
            new UserEntity("valentin_lambev@abv.bg", "Password1", "Valentin", "Lambeva", "9612302211", "0878857543", "M21", UserRole.COMPETITOR, null),
            new UserEntity("valeriya_georgieva@abv.bg", "Password1", "Valeriya", "Georgieva", "9602202211", "0878857543", "M21", UserRole.COMPETITOR, null)
    );*/

    UserEntity admin = new UserEntity("admin@gmail.com", "Password", "Admin", "Admin", "8005203124", UserRole.ADMIN);

    public static void main(String[] args) {
        SpringApplication.run(ServiceAPISpringBoot.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //if (clubService.getAllClubs().isEmpty()) clubs.forEach(clubService::createClub);
        //if (userService.getUsers().isEmpty()) users.forEach(authenticationService::register);
        if(userService.getUsers().isEmpty()) authenticationService.register(admin);
    }
}
