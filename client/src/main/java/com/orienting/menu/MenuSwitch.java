package com.orienting.menu;

import com.orienting.command.Command;
import com.orienting.command.CommandWithInputs;
import com.orienting.common.dto.*;
import com.orienting.context.UserContext;
import com.orienting.controller.*;
import com.orienting.common.enums.UserRole;
import com.orienting.utils.JsonFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MenuSwitch {
    private static final Map<String, Menu> menus = new HashMap<>();

    private static Integer validateId(String id) {
        if (id.matches("\\d+")) {
            return Integer.parseInt(id);
        } else {
            return -1;
        }
    }

    private static boolean isValidDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private static MenuEntry getAllClubs(ClubController clubController, String menu) {
        return new MenuEntry("Get all clubs", new Command(() -> {
            List<ClubDto> result = clubController.getAllClubs();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu(menu);
        }), null);
    }

    private static MenuEntry getAllCompetitions(CompetitionController competitionController, String menu) {
        return new MenuEntry("Get all competitions", new Command(() -> {
            List<CompetitionDto> result = competitionController.getAllCompetitions();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu(menu);
        }), null);
    }

    static {
        AuthenticationController auth = new AuthenticationController();
        UsersController usersController = new UsersController();
        ClubController clubController = new ClubController();
        CompetitionController competitionController = new CompetitionController();
        LogoutController logoutController = new LogoutController();


        MenuEntry goToMain = new MenuEntry("Go to the main menu", new Command(() -> showMenu("main")), null);
        MenuEntry register = new MenuEntry("Register menu: ", new Command(() -> showMenu("register")), null);
        MenuEntry login = new MenuEntry("Login menu: ", new Command(() -> showMenu("login")), null);
        MenuEntry exit = new MenuEntry("Exit ", new Command(() -> {}), null);

        MenuEntry getAllClubsGuest = getAllClubs(clubController, "main");
        MenuEntry getAllCompetitionsGuest = getAllCompetitions(competitionController, "main");

        Menu mainMenu = new Menu("Main Menu", List.of(register, login, getAllClubsGuest, getAllCompetitionsGuest, exit));
        menus.put("main", mainMenu);

        //Registration menu
        Argument userId = new Argument("user id");
        Argument email = new Argument("email");
        Argument password = new Argument("password");
        Argument firstName = new Argument("first name");
        Argument lastName = new Argument("last name");
        Argument ucn = new Argument("ucn");
        Argument phoneNumber = new Argument("phone number");
        Argument group = new Argument("group");
        Argument role = new Argument("role");

        Argument clubId = new Argument("club id");
        Argument clubName = new Argument("club name");
        Argument newClubName = new Argument("new club name");
        Argument city = new Argument("city");

        Argument compId = new Argument("competition id");
        Argument competitionName = new Argument("competition name");
        Argument date = new Argument("date");
        Argument time = new Argument("time");
        Argument deadline = new Argument("deadline");
        Argument location = new Argument("location");
        Argument coordinates = new Argument("coordinates");
        Argument newCompetitionName = new Argument("new competiton name");

        //Register menu
        MenuEntry m1 = new MenuEntry("Sign up", new CommandWithInputs(List.of(email, password, firstName, lastName, ucn, phoneNumber, group, role, clubId, clubName), (args) -> {
            UserCreationDto user = new UserCreationDto(args.get(0).getValue(), args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue(), args.get(7).getValue(), args.get(8).getValue(), args.get(9).getValue());
            AuthenticationResponseDto authResponse = auth.register(user);
            if (authResponse != null) {
                System.out.println("The registration is successful!");
                System.out.println("Access token: " + authResponse.getAccessToken());

                System.out.println(authResponse);
                UserContext.setRole(user.getRole());
                UserContext.setEmail(user.getEmail());
                UserContext.setToken(authResponse.getAccessToken());
                showMenu("menu");
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
                showMenu("menu");
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


        MenuEntry loggedIn = new MenuEntry("Back to the previous menu", new Command(() -> showMenu("menu")), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));

        //Users
        MenuEntry getLoggedInUser = new MenuEntry("Get information about me", new Command(() -> {
            System.out.println(JsonFormatter.formatJson(usersController.getLoggedInUser()));
            showMenu("manage users");
        }), List.of(UserRole.COACH, UserRole.ADMIN, UserRole.COMPETITOR));
        MenuEntry getAllUsers = new MenuEntry("Get all users", new Command(() -> {
            System.out.println(JsonFormatter.formatJson(usersController.getAllUsers()));
            showMenu("manage users");
        }), List.of(UserRole.COACH, UserRole.ADMIN));
        MenuEntry getUserById = new MenuEntry("Get user by id", new CommandWithInputs(List.of(userId), (args) -> {
            UserDto result = usersController.getUserById(validateId(args.get(0).getValue()));
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
        MenuEntry getRoleById = new MenuEntry("Get role by id", new CommandWithInputs(List.of(userId), (args) -> {
            String result = usersController.getRoleById(validateId(args.get(0).getValue()));
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
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getAllCompetitors = new MenuEntry("Get all competitors", new Command(() -> {
            List<CompetitorDto> result = usersController.getAllCompetitors();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry getUserWithCoachesById = new MenuEntry("Get user with coaches by id", new CommandWithInputs(List.of(userId), (args) -> {
            CompetitorsWithCoachesDto result = usersController.getUserWithCoachesById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getLoggedInUserWithCoaches = new MenuEntry("Get information about me and my coaches", new Command(() -> {
            CompetitorsWithCoachesDto result = usersController.getLoggedInUserWithCoaches();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COMPETITOR));
        MenuEntry getUserWithRequestedCompetitionsById = new MenuEntry("Get user by id with requested competitions", new CommandWithInputs(List.of(userId), (args) -> {
            UsersWithRequestedCompetitionsDto result = usersController.getUserWithRequestedCompetitionsById(validateId(args.get(0).getValue()));
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
        MenuEntry getAllUsersInClubById = new MenuEntry("Get all users in club by club id", new CommandWithInputs(List.of(clubId), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllUsersInClubById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCompetitorsInClubById = new MenuEntry("Get all competitors in club by club id", new CommandWithInputs(List.of(clubId), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllCompetitorsInClubById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCoachesInClubById = new MenuEntry("Get all coaches in club by club id", new CommandWithInputs(List.of(clubId), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllCoachesInClubById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllUsersInClubByName = new MenuEntry("Get all users in club by club name", new CommandWithInputs(List.of(clubName), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllUsersInClubByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCompetitorsInClubByName = new MenuEntry("Get all competitors in club by club name", new CommandWithInputs(List.of(clubName), (args) -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllCompetitorsInClubByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry getAllCoachesInClubByName = new MenuEntry("Get all coaches in club by club name", new CommandWithInputs(List.of(clubName), (args) -> {
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
            List<CompetitorsAndCoachDto> result = usersController.getAllCoachesInClub();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry getAllCompetitorsInClub = new MenuEntry("Get all competitors from my club", new Command(() -> {
            List<CompetitorsAndCoachDto> result = usersController.getAllCompetitorsInClub();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));

        MenuEntry deleteUserByUserId = new MenuEntry("Delete user by user id", new CommandWithInputs(List.of(userId), (args) -> {
            UserDto result = usersController.deleteUserByUserId(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry deleteUserByUcn = new MenuEntry("Delete user by ucn", new CommandWithInputs(List.of(ucn), (args) -> {
            UserDto result = usersController.deleteUserByUcn(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry makeCoach = new MenuEntry("Make coach user by user id", new CommandWithInputs(List.of(userId), (args) -> {
            UserDto result = usersController.makeCoach(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry removeCoach = new MenuEntry("Remove coach by user id", new CommandWithInputs(List.of(userId), (args) -> {
            UserDto result = usersController.removeCoach(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry setCoachToClub = new MenuEntry("Add coach to club by given user id and club id", new CommandWithInputs(List.of(userId, clubId), (args) -> {
            UserDto result = usersController.setCoachToClub(validateId(args.get(0).getValue()), validateId(args.get(1).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry updateUserByUserId = new MenuEntry("Update user by user id", new CommandWithInputs(List.of(userId, email, password, firstName, lastName, phoneNumber, group, role, clubId, clubName), (args) -> {
            UserUpdateDto userUpdateDto = new UserUpdateDto(args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue(), args.get(7).getValue(), args.get(8).getValue(), args.get(9).getValue());
            UserDto result = usersController.updateUserByUserId(validateId(args.get(0).getValue()), userUpdateDto);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry updateUserByUcn = new MenuEntry("Update user by ucn", new CommandWithInputs(List.of(ucn, email, password, firstName, lastName, phoneNumber, group, role, clubId, clubName), (args) -> {
            UserUpdateDto userUpdateDto = new UserUpdateDto(args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue(), args.get(7).getValue(), args.get(8).getValue(), args.get(9).getValue());
            UserDto result = usersController.updateUserByUcn(args.get(0).getValue(), userUpdateDto);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry updateLoggedInUser = new MenuEntry("Update", new CommandWithInputs(List.of(email, password, firstName, lastName, phoneNumber, group, role, clubId, clubName), (args) -> {
            UserUpdateDto userUpdateDto = new UserUpdateDto(args.get(0).getValue(), args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue(), args.get(7).getValue(), args.get(8).getValue());
            UserDto result = usersController.updateLoggedInUser(userUpdateDto);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry addUser = new MenuEntry("Add user", new CommandWithInputs(List.of(email, password, firstName, lastName, ucn, phoneNumber, group, role, clubId, clubName), (args) -> {
            UserCreationDto user = new UserCreationDto(args.get(0).getValue(), args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue(), args.get(7).getValue(), args.get(8).getValue(), args.get(9).getValue());
            UserDto result = usersController.addUser(user);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));


        Menu usersMenu = new Menu("Manage users",
                List.of(getLoggedInUser, getAllUsers, getUserById, getUserByUcn, getRole, getRoleById, getRoleByUcn,
                        getAllCoaches, getAllCompetitors, getUserWithCoachesById, getLoggedInUserWithCoaches, getUserWithRequestedCompetitionsById,
                        getLoggedInUserWithRequestedCompetitions, getAllUsersInClubById, getAllCompetitorsInClubById, getAllCoachesInClubById, getAllUsersInClubByName,
                        getAllCompetitorsInClubByName, getAllCoachesInClubByName, getAllUsersInClub, getAllCoachesInClub, getAllCompetitorsInClub,
                        deleteUserByUserId, deleteUserByUcn, makeCoach, removeCoach,
                        setCoachToClub, updateUserByUserId, updateUserByUcn, updateLoggedInUser, addUser, loggedIn, logout));
        menus.put("manage users", usersMenu);

        //Clubs
        MenuEntry getLoginUserClub = new MenuEntry("Get my club", new Command(() -> {
            ClubDto result = usersController.getLoginUserClub();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry getAllClubs = getAllClubs(clubController, "manage clubs");
        MenuEntry getAllClubsWithUsers = new MenuEntry("Get all clubs with users", new Command(() -> {
            List<ClubWithUsersDto> result = clubController.getAllClubsWithUsers();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry getClubById = new MenuEntry("Get club by id", new CommandWithInputs(List.of(clubId), (args) -> {
            ClubDto result = clubController.getClubById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry getClubByName = new MenuEntry("Get club by name", new CommandWithInputs(List.of(clubName), (args) -> {
            ClubDto result = clubController.getClubByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry getClubWithUsersById = new MenuEntry("Get club by id with users", new CommandWithInputs(List.of(clubId), (args) -> {
            ClubWithUsersDto result = clubController.getClubWithUsersById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry getClubWithUsersByName = new MenuEntry("Get club by name with users", new CommandWithInputs(List.of(clubName), (args) -> {
            ClubWithUsersDto result = clubController.getClubWithUsersByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry addClubToUser = new MenuEntry("Add club to user by user id and club id", new CommandWithInputs(List.of(userId, clubId), (args) -> {
            UserDto result = usersController.addClubToUser(validateId(args.get(0).getValue()), validateId(args.get(1).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN));
        MenuEntry addClubToLoggedInUser = new MenuEntry("Add club", new CommandWithInputs(List.of(clubId), (args) -> {
            UserDto result = usersController.addClubToLoggedInUser(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry leftClubById = new MenuEntry("Remove club of user with user id", new CommandWithInputs(List.of(userId), (args) -> {
            UserDto result = usersController.leftClubById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN));
        MenuEntry leftClubLoggedInUser = new MenuEntry("Leave the club", new Command(() -> {
            UserDto result = usersController.leftClubLoggedInUser();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry addClub = new MenuEntry("Add club", new CommandWithInputs(List.of(clubName, city), (args) -> {
            ClubDto club = new ClubDto(args.get(0).getValue(), args.get(1).getValue());
            ClubDto result = clubController.addClub(club);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN));
        MenuEntry deleteClubById = new MenuEntry("Delete club by id", new CommandWithInputs(List.of(clubId), (args) -> {
            ClubDto result = clubController.deleteClubById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN));
        MenuEntry deleteClubByName = new MenuEntry("Delete club by name", new CommandWithInputs(List.of(clubName), (args) -> {
            ClubDto result = clubController.deleteClubByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN));
        MenuEntry updateClubById = new MenuEntry("Update club by id", new CommandWithInputs(List.of(clubId, clubName, city), (args) -> {
            ClubDto club = new ClubDto(args.get(1).getValue(), args.get(2).getValue());
            ClubDto result = clubController.updateClubById(validateId(args.get(0).getValue()), club);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN));
        MenuEntry updateClubByName = new MenuEntry("Update club by name", new CommandWithInputs(List.of(clubName, newClubName, city), (args) -> {
            ClubDto club = new ClubDto(args.get(1).getValue(), args.get(2).getValue());
            ClubDto result = clubController.updateClubByName(args.get(0).getValue(), club);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN));

        Menu clubsMenu = new Menu("Manage clubs",
                List.of(getLoginUserClub, getAllClubs, getAllClubsWithUsers, getClubById, getClubByName, getClubWithUsersById, getClubWithUsersByName, addClubToUser, addClubToLoggedInUser, leftClubById, leftClubLoggedInUser, addClub, deleteClubById, deleteClubByName, updateClubById, updateClubByName, loggedIn, logout));
        menus.put("manage clubs", clubsMenu);


        //Competitions
        MenuEntry getAllCompetitions = getAllCompetitions(competitionController,"manage competitions");
        MenuEntry getAllCompetitionsWithParticipants = new MenuEntry("Get all competitions with participants", new Command(() -> {
            List<CompetitionRequestDto> result = competitionController.getAllCompetitionsWithParticipants();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getCompetitionByDate = new MenuEntry("Get competition by date", new CommandWithInputs(List.of(date), (args) -> {
            if (isValidDate(args.get(0).getValue())) {
                List<CompetitionDto> result = competitionController.getCompetitionByDate(args.get(0).getValue());
                if (result != null)
                    System.out.println(JsonFormatter.formatJson(result));
            } else System.err.println("Invalid date!");
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getCompetitionByDateWithParticipants = new MenuEntry("Get competition by date with participants", new CommandWithInputs(List.of(date), (args) -> {
            if (isValidDate(args.get(0).getValue())) {
                List<CompetitionRequestDto> result = competitionController.getCompetitionByDateWithParticipants(args.get(0).getValue());
                if (result != null)
                    System.out.println(JsonFormatter.formatJson(result));
            } else System.err.println("Invalid date!");
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getCompetitionByName = new MenuEntry("Get competition by name", new CommandWithInputs(List.of(competitionName), (args) -> {
            CompetitionDto result = competitionController.getCompetitionByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getCompetitionByNameWithParticipants = new MenuEntry("Get competition by name with participants", new CommandWithInputs(List.of(competitionName), (args) -> {
            CompetitionRequestDto result = competitionController.getCompetitionByNameWithParticipants(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getCompetitionById = new MenuEntry("Get competition by id", new CommandWithInputs(List.of(compId), (args) -> {
            CompetitionDto result = competitionController.getCompetitionById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getCompetitionByIdWithParticipants = new MenuEntry("Get competition by id with participants", new CommandWithInputs(List.of(compId), (args) -> {
            CompetitionRequestDto result = competitionController.getCompetitionByIdWithParticipants(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry createCompetition = new MenuEntry("Create competition", new CommandWithInputs(List.of(competitionName, date, time, deadline, location, coordinates), (args) -> {
            CompetitionDto competition = new CompetitionDto(args.get(0).getValue(), args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue());
            CompetitionDto result = competitionController.createCompetition(competition);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN));
        MenuEntry deleteCompetitionById = new MenuEntry("Delete competition by id", new CommandWithInputs(List.of(compId), (args) -> {
            CompetitionDto result = competitionController.deleteCompetitionById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN));
        MenuEntry deleteCompetitionByName = new MenuEntry("Delete competition by name", new CommandWithInputs(List.of(competitionName), (args) -> {
            CompetitionDto result = competitionController.deleteCompetitionByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN));
        MenuEntry updateCompetitionByCompId = new MenuEntry("Update competition by id", new CommandWithInputs(List.of(compId, competitionName, date, time, deadline, location, coordinates), (args) -> {
            CompetitionUpdateDto competition = new CompetitionUpdateDto(args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue());
            CompetitionDto result = competitionController.updateCompetitionByCompId(validateId(args.get(0).getValue()), competition);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN));
        MenuEntry updateCompetitionByName = new MenuEntry("Update competition by name", new CommandWithInputs(List.of(competitionName, newCompetitionName, date, time, deadline, location, coordinates), (args) -> {
            CompetitionUpdateDto competition = new CompetitionUpdateDto(args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue());
            CompetitionDto result = competitionController.updateCompetitionByName(args.get(0).getValue(), competition);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN));
        MenuEntry requestParticipationById = new MenuEntry("Request participation of user by user id and competition id", new CommandWithInputs(List.of(userId, compId), (args) -> {
            UserDto result = competitionController.requestParticipationById(validateId(args.get(0).getValue()), validateId(args.get(1).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry requestParticipationByName = new MenuEntry("Request participation of user by user id and competition name", new CommandWithInputs(List.of(userId, competitionName), (args) -> {
            UserDto result = competitionController.requestParticipationByName(validateId(args.get(0).getValue()), args.get(1).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry requestLoggedInUserParticipationById = new MenuEntry("Request participation by competition id", new CommandWithInputs(List.of(compId), (args) -> {
            UserDto result = competitionController.requestLoggedInUserParticipationById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry requestLoggedInUserParticipationByName = new MenuEntry("Request participation by competition name", new CommandWithInputs(List.of(competitionName), (args) -> {
            UserDto result = competitionController.requestLoggedInUserParticipationByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry removeParticipationById = new MenuEntry("Remove participation of user by user id and competition id", new CommandWithInputs(List.of(userId, compId), (args) -> {
            UserDto result = competitionController.removeParticipationById(validateId(args.get(0).getValue()), validateId(args.get(1).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry removeParticipationByName = new MenuEntry("Remove participation of user by user id and competition name", new CommandWithInputs(List.of(userId, competitionName), (args) -> {
            UserDto result = competitionController.removeParticipationByName(validateId(args.get(0).getValue()), args.get(1).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry removeLoggedInUserParticipationById = new MenuEntry("Remove participation by competition id", new CommandWithInputs(List.of(compId), (args) -> {
            UserDto result = competitionController.removeLoggedInUserParticipationById(validateId(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));
        MenuEntry removeLoggedInUserParticipationByName = new MenuEntry("Remove participation by competition name", new CommandWithInputs(List.of(competitionName), (args) -> {
            UserDto result = competitionController.removeLoggedInUserParticipationByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.COMPETITOR, UserRole.COACH));

        Menu competitionMenu = new Menu("Manage competitions",
                List.of(getAllCompetitions, getAllCompetitionsWithParticipants, getCompetitionByDate, getCompetitionByDateWithParticipants, getCompetitionByName, getCompetitionByNameWithParticipants, getCompetitionById, getCompetitionByIdWithParticipants, createCompetition, deleteCompetitionById, deleteCompetitionByName, updateCompetitionByCompId, updateCompetitionByName,
                        requestParticipationById, requestParticipationByName, requestLoggedInUserParticipationById, requestLoggedInUserParticipationByName, removeParticipationById, removeParticipationByName, removeLoggedInUserParticipationById, removeLoggedInUserParticipationByName, loggedIn, logout));
        menus.put("manage competitions", competitionMenu);


        MenuEntry menuUsers = new MenuEntry("Menu users", new Command(() -> showMenu("manage users")),List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry menuClubs = new MenuEntry("Menu clubs", new Command(() -> showMenu("manage clubs")), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry menuCompetitions = new MenuEntry("Menu competitions", new Command(() -> showMenu("manage competitions")), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));

        Menu loggedInUser = new Menu("My menu", List.of(menuUsers, menuClubs, menuCompetitions, logout));
        menus.put("menu", loggedInUser);
    }

    public static void showMenu(String name) {
        menus.get(name).show();
    }
}
