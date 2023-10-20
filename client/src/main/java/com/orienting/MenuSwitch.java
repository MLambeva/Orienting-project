package com.orienting;

import com.orienting.common.dto.*;
import com.orienting.controller.AuthenticationController;
import com.orienting.controller.LogoutController;
import com.orienting.controller.UsersController;
import com.orienting.common.enums.UserRole;

import java.util.*;
import java.util.List;

public class MenuSwitch {
    private static final Map<String, Menu> menus = new HashMap<>();

    static {
        AuthenticationController auth = new AuthenticationController();
        UsersController usersController = new UsersController();
        LogoutController logoutController = new LogoutController();


        MenuEntry goToMain = new MenuEntry("Go to the main menu", new Command(() -> showMenu("main")), null);
        MenuEntry register = new MenuEntry("Register menu: ", new Command(() -> {
            showMenu("register");
        }), null);
        MenuEntry login = new MenuEntry("Login menu: ", new Command(() -> {
            showMenu("login");
        }), null);
        MenuEntry exit = new MenuEntry("Exit ", new Command(() -> {
        }), null);


        Menu mainMenu = new Menu("Main Menu", List.of(register, login, exit));
        menus.put("main", mainMenu);

        //Registration menu
        Argument email = new Argument("email");
        Argument password = new Argument("password");
        Argument firstName = new Argument("first name");
        Argument lastName = new Argument("last name");
        Argument ucn = new Argument("ucn");
        Argument phoneNumber = new Argument("phone number");
        Argument group = new Argument("group");
        Argument role = new Argument("role");
        Argument clubId = new Argument("club`s id");

        Argument id = new Argument("id");
        Argument clubName = new Argument("club`s name");

        MenuEntry m1 = new MenuEntry("Sign up", new CommandWithInputs(List.of(email, password, firstName, lastName, ucn, phoneNumber, group, role, clubId), (args) -> {
            UserCreationDto user = new UserCreationDto(args.get(0).getValue(), args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue(), args.get(7).getValue(), (args.get(8).getValue().matches("\\d+")) ? Integer.parseInt(args.get(8).getValue()) : null);
            AuthenticationResponseDto authResponse = auth.register(user);
            if (authResponse != null) {
                System.out.println("The registration is successful!");
                System.out.println("Access token: " + authResponse.getAccessToken());

                System.out.println(authResponse);
                UserContext.setRole(user.getRole());
                UserContext.setEmail(user.getEmail());
                UserContext.setToken(authResponse.getAccessToken());
                showMenu("manage users");
            } else {
                showMenu("register");
            }
        }), null);
        Menu registerMenu = new Menu("Register menu", List.of(m1, goToMain));
        menus.put("register", registerMenu);

        //Login menu
        MenuEntry m2 = new MenuEntry("Sign in", new CommandWithInputs(List.of(email, password), (args) -> {
            SignInDto user = new SignInDto(args.get(0).getValue(), args.get(1).getValue());
            AuthenticationResponseDto authResponse = auth.authenticate(user);
            if (authResponse != null) {
                System.out.println("Authentication is successful!");
                System.out.println("Access token: " + authResponse.getAccessToken());

                UserContext.setToken(authResponse.getAccessToken());
                UserContext.setRole(usersController.getLoggedInUser().getRole());
                UserContext.setEmail(user.getEmail());
                showMenu("manage users");
            } else {
                showMenu("login");
            }
        }), null);
        Menu authMenu = new Menu("Authentication menu", List.of(m2, goToMain));
        menus.put("login", authMenu);

        //Logout menu
        MenuEntry logout = new MenuEntry("Logout", new Command(() -> {
            logoutController.logout();
            showMenu("main");
        }), List.of(UserRole.COACH, UserRole.ADMIN, UserRole.COMPETITOR));
        Menu logoutMenu = new Menu("Logout menu", List.of(logout));
        menus.put("logout", logoutMenu);

        MenuEntry getLoggedInUser = new MenuEntry("Get information about me", new Command(() -> {
            System.out.println(JsonFormatter.formatJson(usersController.getLoggedInUser()));
            showMenu("manage users");
        }), List.of(UserRole.COACH, UserRole.ADMIN, UserRole.COMPETITOR));
        MenuEntry getAllUsers = new MenuEntry("Get all users", new Command(() -> {
            System.out.println(JsonFormatter.formatJson(usersController.getAllUsers()));
            showMenu("manage users");
        }), List.of(UserRole.COACH, UserRole.ADMIN));
        MenuEntry getUserById = new MenuEntry("Get user by id", new CommandWithInputs(List.of(id), (args) -> {
            UserDto result = usersController.getUserById(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getUserByUcn = new MenuEntry("Get user by ucn", new CommandWithInputs(List.of(ucn), (args) -> {
            UserDto result = usersController.getUserByUcn(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getRole = new MenuEntry("Get my role", new Command(() -> {
            System.out.println(JsonFormatter.formatJson(usersController.getLoggedInUserRole()));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getRoleById = new MenuEntry("Get role by id", new CommandWithInputs(List.of(id), (args) -> {
            String result = usersController.getRoleById(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getRoleByUcn = new MenuEntry("Get role by ucn", new CommandWithInputs(List.of(ucn), (args) -> {
            String result = usersController.getRoleByUcn(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCoaches = new MenuEntry("Get all coaches", new Command(() -> {
            List<CompetitorDto> result = usersController.getAllCoaches();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCompetitors = new MenuEntry("Get all competitors", new Command(() -> {
            List<CompetitorDto> result = usersController.getAllCompetitors();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getUserWithCoachesById = new MenuEntry("Get user with coaches by id", new CommandWithInputs(List.of(id), (args) -> {
            CompetitorsWithCoachesDto result = usersController.getUserWithCoachesById(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getLoggedInUserWithCoaches = new MenuEntry("Get information about me and my coaches", new Command(() -> {
            CompetitorsWithCoachesDto result = usersController.getLoggedInUserWithCoaches();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getUserWithRequestedCompetitionsById = new MenuEntry("Get user by id with requested competitions", new CommandWithInputs(List.of(id), (args) -> {
            UsersWithRequestedCompetitionsDto result = usersController.getUserWithRequestedCompetitionsById(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getLoggedInUserWithRequestedCompetitions = new MenuEntry("Get information about me and my requested competitions", new Command(() -> {
            UsersWithRequestedCompetitionsDto result = usersController.getLoggedInUserWithRequestedCompetitions();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getAllUsersInClubById = new MenuEntry("Get all users in club by club id", new CommandWithInputs(List.of(id), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllUsersInClubById(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCompetitorsInClubById = new MenuEntry("Get all competitors in club by id", new CommandWithInputs(List.of(id), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllCompetitorsInClubById(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCoachesInClubById = new MenuEntry("Get all coaches in club by id", new CommandWithInputs(List.of(id), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllCoachesInClubById(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllUsersInClubByName = new MenuEntry("Get all users in club by club`s name", new CommandWithInputs(List.of(clubName), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllUsersInClubByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCompetitorsInClubByName = new MenuEntry("Get all competitors in club by club`s name", new CommandWithInputs(List.of(clubName), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllCompetitorsInClubByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCoachesInClubByName = new MenuEntry("Get all coaches in club by club`s name", new CommandWithInputs(List.of(clubName), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllCoachesInClubByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllUsersInClub = new MenuEntry("Get all users from my club", new Command(() -> {
            AllUsersInClubDto result = usersController.getAllUsersInClub();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry getAllCoachesInClub = new MenuEntry("Get all coaches from my club", new Command(() -> {
            AllUsersInClubDto result = usersController.getAllCoachesInClub();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry getAllCompetitorsInClub = new MenuEntry("Get all competitors from my club", new Command(() -> {
            AllUsersInClubDto result = usersController.getAllCompetitorsInClub();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry getLoginUserClub = new MenuEntry("Get my club", new Command(() -> {
            ClubDto result = usersController.getLoginUserClub();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry deleteUserByUserId = new MenuEntry("Delete user by id", new CommandWithInputs(List.of(id), (args) -> {
            UserDto result = usersController.deleteUserByUserId(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry deleteUserByUcn = new MenuEntry("Delete user by ucn", new CommandWithInputs(List.of(ucn), (args) -> {
            UserDto result = usersController.deleteUserByUcn(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry leftClubById = new MenuEntry("Remove club of user with id", new CommandWithInputs(List.of(id), (args) -> {
            UserDto result = usersController.leftClubById(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry leftClubLoggedInUser = new MenuEntry("Leave the club", new Command(() -> {
            UserDto result = usersController.leftClubLoggedInUser();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry makeCoach = new MenuEntry("Make coach user by id", new CommandWithInputs(List.of(id), (args) -> {
            UserDto result = usersController.makeCoach(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry removeCoach = new MenuEntry("Remove coach by id", new CommandWithInputs(List.of(id), (args) -> {
            UserDto result = usersController.removeCoach(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));


        Menu usersMenu = new Menu("Manage users", List.of(getLoggedInUser, getAllUsers, getUserById, getUserByUcn, getRole, getRoleById, getRoleByUcn, logout, exit));
        menus.put("manage users", usersMenu);


    }

    public static void showMenu(String name) {
        menus.get(name).show();
    }
}
