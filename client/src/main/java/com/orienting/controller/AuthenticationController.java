package com.orienting.controller;

import com.orienting.common.dto.AuthenticationResponseDto;
import com.orienting.common.dto.SignInDto;
import com.orienting.common.dto.UserCreationDto;

public class AuthenticationController extends MainController {
    private final String AUTH_URL = "http://localhost:8080/api/auth";

    public AuthenticationResponseDto register(UserCreationDto input) {
        return (AuthenticationResponseDto) makeRequest(input, AUTH_URL + "/register", "POST",  new AuthenticationResponseDto());
    }

    public AuthenticationResponseDto authenticate(SignInDto input) {
        return (AuthenticationResponseDto) makeRequest(input, AUTH_URL + "/authenticate", "POST",  new AuthenticationResponseDto());
    }

    /*private SignInDto getAuthUserInput() {
        Scanner input = new Scanner(System.in);
        SignInDto user = new SignInDto();

        System.out.print("Write email: ");
        user.setEmail(input.nextLine());
        System.out.print("Write password: ");
        user.setPassword(input.nextLine());

        return user;
    }

    private UserCreationDto getRegisterUserInput() {
        Scanner input = new Scanner(System.in);
        UserCreationDto user = new UserCreationDto();

        System.out.print("Write email: ");
        user.setEmail(input.nextLine());
        System.out.print("Write password: ");
        user.setPassword(input.nextLine());
        System.out.print("Write first name: ");
        user.setFirstName(input.nextLine());
        System.out.print("Write last name: ");
        user.setLastName(input.nextLine());
        System.out.print("Write UCN: ");
        user.setUcn(input.nextLine());
        System.out.print("Write group: ");
        user.setGroup(input.nextLine());
        System.out.print("Write role (COACH/COMPETITOR/ADMIN): ");
        String role = input.nextLine();

        switch (role) {
            case "COACH" -> user.setRole(UserRole.COACH);
            case "COMPETITOR" -> user.setRole(UserRole.COMPETITOR);
            case "ADMIN" -> user.setRole(UserRole.ADMIN);
        }

        System.out.print("Write club id: ");
        if (input.hasNextInt())
            user.setClubId(input.nextInt());

        return user;
    }*/

}
