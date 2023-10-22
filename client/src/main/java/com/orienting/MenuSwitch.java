package com.orienting;

import com.orienting.common.dto.*;
import com.orienting.controller.*;
import com.orienting.common.enums.UserRole;

import java.util.*;
import java.util.List;

public class MenuSwitch {
    private static final Map<String, Menu> menus = new HashMap<>();

    static {
        AuthenticationController auth = new AuthenticationController();
        UsersController usersController = new UsersController();
        ClubController clubController = new ClubController();
        CompetitionController competitionController = new CompetitionController();
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
        Argument newClubName = new Argument("new club`s name");
        Argument city = new Argument("city");

        Argument compId = new Argument("competition`s id");
        Argument competitionName = new Argument("competition`s name");
        Argument date = new Argument("date");
        Argument time = new Argument("time");
        Argument deadline = new Argument("deadline");
        Argument location = new Argument("location");
        Argument coordinates = new Argument("coordinates");
        Argument newCompetitionName = new Argument("new competiton`s name");

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


        MenuEntry loggedIn = new MenuEntry("Back to whole menu", new Command(() -> showMenu("menu")), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));

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
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getAllCompetitors = new MenuEntry("Get all competitors", new Command(() -> {
            List<CompetitorDto> result = usersController.getAllCompetitors();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
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
        }), List.of(UserRole.COMPETITOR));
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
        }), List.of(UserRole.ADMIN, UserRole.COACH));
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
        MenuEntry setCoachToClub = new MenuEntry("Add coach to club by given id", new CommandWithInputs(List.of(id, id), (args) -> {
            UserDto result = usersController.setCoachToClub(Integer.parseInt(args.get(0).getValue()), Integer.parseInt(args.get(1).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry addClubToUser = new MenuEntry("Add user`s club by user and club id", new CommandWithInputs(List.of(id, id), (args) -> {
            UserDto result = usersController.addClubToUser(Integer.parseInt(args.get(0).getValue()), Integer.parseInt(args.get(1).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN));
        MenuEntry addClubToLoggedInUser = new MenuEntry("Add club", new CommandWithInputs(List.of(id), (args) -> {
            UserDto result = usersController.addClubToLoggedInUser(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry updateUserByUserId = new MenuEntry("Update user by id", new CommandWithInputs(List.of(id, email, password, firstName, lastName, phoneNumber, group, role, clubId), (args) -> {
            UserUpdateDto userUpdateDto = new UserUpdateDto(args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue(), args.get(7).getValue(), (args.get(8).getValue().matches("\\d+")) ? Integer.parseInt(args.get(8).getValue()) : null);
            UserDto result = usersController.updateUserByUserId(Integer.parseInt(args.get(0).getValue()), userUpdateDto);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry updateUserByUcn = new MenuEntry("Update user by ucn", new CommandWithInputs(List.of(ucn, email, password, firstName, lastName, phoneNumber, group, role, clubId), (args) -> {
            UserUpdateDto userUpdateDto = new UserUpdateDto(args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue(), args.get(7).getValue(), (args.get(8).getValue().matches("\\d+")) ? Integer.parseInt(args.get(8).getValue()) : null);
            UserDto result = usersController.updateUserByUcn(args.get(0).getValue(), userUpdateDto);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry updateLoggedInUser = new MenuEntry("Update", new CommandWithInputs(List.of(email, password, firstName, lastName, phoneNumber, group, role, clubId), (args) -> {
            UserUpdateDto userUpdateDto = new UserUpdateDto(args.get(0).getValue(), args.get(1).getValue(), args.get(2).getValue(), args.get(3).getValue(), args.get(4).getValue(), args.get(5).getValue(), args.get(6).getValue(), (args.get(7).getValue().matches("\\d+")) ? Integer.parseInt(args.get(7).getValue()) : null);
            UserDto result = usersController.updateLoggedInUser(userUpdateDto);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage users");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));

        Menu usersMenu = new Menu("Manage users",
                List.of(getLoggedInUser, getAllUsers, getUserById, getUserByUcn, getRole, getRoleById, getRoleByUcn,
                        getAllCoaches, getAllCompetitors, getUserWithCoachesById, getLoggedInUserWithCoaches, getUserWithRequestedCompetitionsById,
                        getLoggedInUserWithRequestedCompetitions, getAllUsersInClubById, getAllCompetitorsInClubById, getAllCoachesInClubById, getAllUsersInClubByName,
                        getAllCompetitorsInClubByName, getAllCoachesInClubByName, getAllUsersInClub, getAllCoachesInClub, getAllCompetitorsInClub, getLoginUserClub,
                        deleteUserByUserId, deleteUserByUcn, leftClubById, leftClubLoggedInUser, makeCoach, removeCoach,
                        setCoachToClub, addClubToUser, addClubToLoggedInUser, updateUserByUserId, updateUserByUcn, updateLoggedInUser, loggedIn, logout));
        menus.put("manage users", usersMenu);

        MenuEntry getAllClubs = new MenuEntry("Get all clubs", new Command(() -> {
            List<ClubDto> result = clubController.getAllClubs();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), null);
        MenuEntry getAllClubsWithUsers = new MenuEntry("Get all clubs with users", new Command(() -> {
            List<ClubWithUsersDto> result = clubController.getAllClubsWithUsers();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry getClubById = new MenuEntry("Get club by id", new CommandWithInputs(List.of(clubId), (args) -> {
            ClubDto result = clubController.getClubById(Integer.parseInt(args.get(0).getValue()));
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
            ClubWithUsersDto result = clubController.getClubWithUsersById(Integer.parseInt(args.get(0).getValue()));
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
        MenuEntry addClub = new MenuEntry("Add club", new CommandWithInputs(List.of(clubName, city), (args) -> {
            ClubDto club = new ClubDto(args.get(0).getValue(), args.get(1).getValue());
            ClubDto result = clubController.addClub(club);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage clubs");
        }), List.of(UserRole.ADMIN));
        MenuEntry deleteClubById = new MenuEntry("Delete club by id", new CommandWithInputs(List.of(clubId), (args) -> {
            ClubDto result = clubController.deleteClubById(Integer.parseInt(args.get(0).getValue()));
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
            ClubDto result = clubController.updateClubById(Integer.parseInt(args.get(0).getValue()), club);
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
                List.of(getAllClubs, getAllClubsWithUsers, getClubById, getClubByName, getClubWithUsersById, getClubWithUsersByName, addClub, deleteClubById, deleteClubByName, updateClubById, updateClubByName, loggedIn, logout));
        menus.put("manage clubs", clubsMenu);

        MenuEntry getAllCompetitions = new MenuEntry("Get all competitions", new Command(() -> {
            List<CompetitionDto> result =  competitionController.getAllCompetitions();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), null);
        MenuEntry getAllCompetitionsWithParticipants = new MenuEntry("Get all competitions with participants", new Command( () -> {
            List<CompetitionRequestDto> result = competitionController.getAllCompetitionsWithParticipants();
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR));
        MenuEntry getCompetitionByDate = new MenuEntry("Get competition by date", new CommandWithInputs(List.of(date), (args) -> {
            List<CompetitionDto> result = competitionController.getCompetitionByDate(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH,  UserRole.COMPETITOR));
        MenuEntry getCompetitionByDateWithParticipants = new MenuEntry("Get competition by date with participants", new CommandWithInputs(List.of(date), (args) -> {
            List<CompetitionRequestDto> result = competitionController.getCompetitionByDateWithParticipants(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH,  UserRole.COMPETITOR));
        MenuEntry getCompetitionByName = new MenuEntry("Get competition by name", new CommandWithInputs(List.of(competitionName), (args) -> {
            CompetitionDto result = competitionController.getCompetitionByName(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH,  UserRole.COMPETITOR));
        MenuEntry getCompetitionByNameWithParticipants = new MenuEntry("Get competition by name with participants", new CommandWithInputs(List.of(competitionName), (args) -> {
            CompetitionRequestDto result = competitionController.getCompetitionByNameWithParticipants(args.get(0).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH,  UserRole.COMPETITOR));
        MenuEntry getCompetitionById = new MenuEntry("Get competition by id", new CommandWithInputs(List.of(id), (args) -> {
            CompetitionDto result = competitionController.getCompetitionById(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH,  UserRole.COMPETITOR));
        MenuEntry getCompetitionByIdWithParticipants = new MenuEntry("Get competition by id with participants", new CommandWithInputs(List.of(id), (args) -> {
            CompetitionRequestDto result = competitionController.getCompetitionByIdWithParticipants(Integer.parseInt(args.get(0).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH,  UserRole.COMPETITOR));
        MenuEntry createCompetition = new MenuEntry("Create competition", new CommandWithInputs(List.of(competitionName, date, time, deadline, location, coordinates), (args) -> {
            CompetitionDto competition = new CompetitionDto(args.get(0).getValue(),args.get(1).getValue(),args.get(2).getValue(),args.get(3).getValue(),args.get(4).getValue(),args.get(5).getValue());
            CompetitionDto result = competitionController.createCompetition(competition);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN));
        MenuEntry deleteCompetitionById = new MenuEntry("Delete competition by id", new CommandWithInputs(List.of(id), (args) -> {
            CompetitionDto result = competitionController.deleteCompetitionById(Integer.parseInt(args.get(0).getValue()));
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
        MenuEntry updateCompetitionByCompId = new MenuEntry("Update competition by id", new CommandWithInputs(List.of(id, competitionName, date, time, deadline, location, coordinates), (args) -> {
            CompetitionUpdateDto competition = new CompetitionUpdateDto(args.get(1).getValue(),args.get(2).getValue(),args.get(3).getValue(),args.get(4).getValue(),args.get(5).getValue(),args.get(6).getValue());
            CompetitionDto result = competitionController.updateCompetitionByCompId(Integer.parseInt(args.get(0).getValue()), competition);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN));
        MenuEntry updateCompetitionByName = new MenuEntry("Update competition by name", new CommandWithInputs(List.of(competitionName, newCompetitionName, date, time, deadline, location, coordinates), (args) -> {
            CompetitionUpdateDto competition = new CompetitionUpdateDto(args.get(1).getValue(),args.get(2).getValue(),args.get(3).getValue(),args.get(4).getValue(),args.get(5).getValue(),args.get(6).getValue());
            CompetitionDto result = competitionController.updateCompetitionByName(args.get(0).getValue(), competition);
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN));
        MenuEntry requestParticipationById = new MenuEntry("Request participation of user by user id and competition id", new CommandWithInputs(List.of(id, compId), (args) -> {
            UserDto result = competitionController.requestParticipationById(Integer.parseInt(args.get(0).getValue()), Integer.parseInt(args.get(1).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry requestParticipationByName = new MenuEntry("Request participation of user by user id and competition name", new CommandWithInputs(List.of(id, competitionName), (args) -> {
            UserDto result = competitionController.requestParticipationByName(Integer.parseInt(args.get(0).getValue()), args.get(1).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry requestLoggedInUserParticipationById = new MenuEntry("Request participation by competition id", new CommandWithInputs(List.of(compId), (args) -> {
            UserDto result = competitionController.requestLoggedInUserParticipationById(Integer.parseInt(args.get(0).getValue()));
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
        MenuEntry removeParticipationById = new MenuEntry("Remove participation of user by user id and competition id", new CommandWithInputs(List.of(id, compId), (args) -> {
            UserDto result = competitionController.removeParticipationById(Integer.parseInt(args.get(0).getValue()), Integer.parseInt(args.get(1).getValue()));
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry removeParticipationByName = new MenuEntry("Remove participation of user by user id and competition name", new CommandWithInputs(List.of(id, competitionName), (args) -> {
            UserDto result = competitionController.removeParticipationByName(Integer.parseInt(args.get(0).getValue()), args.get(1).getValue());
            if (result != null)
                System.out.println(JsonFormatter.formatJson(result));
            showMenu("manage competitions");
        }), List.of(UserRole.ADMIN, UserRole.COACH));
        MenuEntry removeLoggedInUserParticipationById = new MenuEntry("Remove participation by competition id", new CommandWithInputs(List.of(compId), (args) -> {
            UserDto result = competitionController.removeLoggedInUserParticipationById(Integer.parseInt(args.get(0).getValue()));
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

        MenuEntry menuUsers = new MenuEntry("Menu users", new Command(() -> {
            showMenu("manage users");
        }),
                List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR)
        );
        MenuEntry menuClubs = new MenuEntry("Menu clubs", new Command(() -> {
            showMenu("manage clubs");
        }),
                List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR)
        );
        MenuEntry menuCompetitions = new MenuEntry("Menu competitions", new Command(() -> {
            showMenu("manage competitions");
        }),
                List.of(UserRole.ADMIN, UserRole.COACH, UserRole.COMPETITOR)
        );

        Menu loggedInUser = new Menu("My menu", List.of(menuUsers, menuClubs, menuCompetitions, logout));
        menus.put("menu", loggedInUser);

    }

    public static void showMenu(String name) {
        menus.get(name).show();
    }
}
