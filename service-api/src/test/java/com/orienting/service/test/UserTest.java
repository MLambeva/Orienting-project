package com.orienting.service.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.orienting.common.dto.CompetitionDto;
import com.orienting.common.dto.UserCreationDto;
import com.orienting.common.entity.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class UserTest {

    private final String USERS_API = "http://localhost:8080/api/users/";
    private final String CLUBS_API = "http://localhost:8080/api/clubs/";
    private final String AUTH_URI = "http://localhost:8080/api/auth/";
    private final String COMP_URI = "http://localhost:8080/api/competitions/";

    private HttpResponse<String> getHttpResponse(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient
                .newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String getToken(String email, String password) throws IOException, URISyntaxException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(AUTH_URI + "authenticate"))
                .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = getHttpResponse(request);
        if (response.statusCode() >= 400) {
            Assertions.fail(String.format("Unsuccessful Login [%d]", response.statusCode()));
        }
        JsonNode authNode = new ObjectMapper().readTree(response.body());
        return String.valueOf(authNode.get("access_token").textValue());
    }

    private String singInAdmin() throws IOException, URISyntaxException, InterruptedException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/application.properties"));

        String email = properties.getProperty("api.auth.email");
        String password = properties.getProperty("api.auth.password");
        return getToken(email, password);
    }

    private String get(String url, String token, String getting) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(url))
                .GET()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> usersResponse = getHttpResponse(request);

        if (usersResponse.statusCode() >= 400) {
            Assertions.fail(String.format("Unsuccessful getting of %s [%d]", getting, usersResponse.statusCode()));
        }
        return usersResponse.body();
    }


    private String add(String url, String jsonBody, String token, String adding) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> usersResponse = getHttpResponse(request);

        if (usersResponse.statusCode() >= 400) {
            Assertions.fail(String.format("Unsuccessful adding of %s [%d]", adding, usersResponse.statusCode()));
        }
        return usersResponse.body();
    }

    private String addClub(String clubName, String city, String token) throws IOException, URISyntaxException, InterruptedException {
        return add(CLUBS_API + "add", "{\"clubName\":\"" + clubName + "\",\"city\":\"" + city + "\"}", token, "club");
    }

    private void addUser(UserCreationDto user, String token) throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(user);
        add(AUTH_URI + "register", jsonBody, token, "user");
    }

    private void addCompetition(CompetitionDto competition, String token) throws IOException, URISyntaxException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String jsonBody = objectMapper.writeValueAsString(competition);
        add(COMP_URI + "add", jsonBody, token, "competiiton");
    }

    private String update(String url, String jsonBody, String token) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(url))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> usersResponse = getHttpResponse(request);

        if (usersResponse.statusCode() >= 400) {
            Assertions.fail(String.format("Unsuccessful updating [%d]", usersResponse.statusCode()));
        }
        return usersResponse.body();
    }

    private void remove(String url, String token, String deletion) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(url))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .build();

        HttpResponse<String> usersResponse = getHttpResponse(request);

        if (usersResponse.statusCode() >= 400) {
            Assertions.fail(String.format("Unsuccessful deleting of %s [%d]", deletion, usersResponse.statusCode()));
        }
    }

    private void removeClub(String name, String token) throws IOException, URISyntaxException, InterruptedException {
        remove(CLUBS_API + "delete/byName/" + name, token, "club");
    }

    private void removeUser(String ucn, String token) throws IOException, URISyntaxException, InterruptedException {
        remove(USERS_API + "remove/byUcn/" + ucn, token, "user");
    }

    private void removeCompetition(String name, String token) throws URISyntaxException, IOException, InterruptedException {
        remove(COMP_URI + "delete/byName/" + name, token, "competition");
    }

    @Test
    public void addClubsAndUsers() throws URISyntaxException, IOException, InterruptedException {
        String token = singInAdmin();

        String club = addClub("Bacho Kiro", "Dryanovo", token);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(club);

        Integer clubId = jsonNode.get("clubId").asInt();

        addClub("Uzana", "Gabrovo", token);
        addClub("Valdi", "Sofia", token);

        addUser(new UserCreationDto("ivan_ivanov@abv.bg", "Password1", "Ivan", "Ivanov", "0042302211", "0879856542", "M35", UserRole.COACH, clubId), token);
        addUser(new UserCreationDto("martina_hristova@abv.bg", "Password1", "Martina", "Hristova", "9042302211", "0899856542", "W21", UserRole.COACH, clubId), token);
        addUser(new UserCreationDto("petur_petrov@abv.bg", "Password1", "Petur", "Petrov", "9402302211", "0889856542", "M21", UserRole.COMPETITOR, clubId), token);
        addUser(new UserCreationDto("ivailo_ivanov@abv.bg", "Password1", "Ivailo", "Ivanov", "8002302211", "0888856542", "M40", UserRole.COMPETITOR, clubId), token);
        addUser(new UserCreationDto("mariyan_ivanov@abv.bg", "Password1", "Mariyan", "Ivanov", "9602302211", "0878857543", "M21", UserRole.COMPETITOR, clubId), token);
        addUser(new UserCreationDto("marina_petrova@abv.bg", "Password1", "Marina", "Petrova", "8402302211", "0878858543", "W35", UserRole.COACH, null), token);
        addUser(new UserCreationDto("monika_ivanova@abv.bg", "Password1", "Monika", "Ivanova", "9102302211", "0878887543", "W21", UserRole.COMPETITOR, null), token);
        addUser(new UserCreationDto("valentin_lambev@abv.bg", "Password1", "Valentin", "Lambeva", "9612302211", "0878857543", "M21", UserRole.COMPETITOR, null), token);
        addUser(new UserCreationDto("valeriya_georgieva@abv.bg", "Password1", "Valeriya", "Georgieva", "9602202211", "0878857543", "M21", UserRole.COMPETITOR, null), token);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        addCompetition(new CompetitionDto("Turnovo cup", LocalDate.parse("16-03-2024", dateFormatter), LocalTime.parse("13:00:00", timeFormatter), LocalDate.parse("13-03-2024", dateFormatter), "Veliko Turnovo", "245N651E"), token);
        addCompetition(new CompetitionDto("Ruse cup", LocalDate.parse("16-03-2024", dateFormatter), LocalTime.parse("16:00:00", timeFormatter), LocalDate.parse("13-03-2024", dateFormatter), "Ruse", "245N661E"), token);
        addCompetition(new CompetitionDto("Dryanovo cup", LocalDate.parse("13-11-2023", dateFormatter), LocalTime.parse("10:00:00", timeFormatter), LocalDate.parse("09-11-2023", dateFormatter), "Dryanovo", "255N651E"), token);
        addCompetition(new CompetitionDto("Sofia cup", LocalDate.parse("02-02-2024", dateFormatter), LocalTime.parse("16:00:00", timeFormatter), LocalDate.parse("31-01-2024", dateFormatter), "Sofia", "265N651E"), token);

    }

    @Test
    public void getAllUsersTest() throws URISyntaxException, IOException, InterruptedException {
        String token = singInAdmin();

        String allUsers = get(USERS_API + "all", token, "all users");
        String[] lines = allUsers.split("},");
        for (String line : lines) {
            System.out.println(line);
        }
    }

    @Test
    public void getAllClubsTest() throws URISyntaxException, IOException, InterruptedException {
        String token = singInAdmin();

        String allClubs = get(CLUBS_API + "all", token, "all clubs");
        String[] lines = allClubs.split("},");
        for (String line : lines) {
            System.out.println(line);
        }
    }

    @Test
    public void getAllCompetitionsTest() throws URISyntaxException, IOException, InterruptedException {
        String token = singInAdmin();

        String allCompetitions = get(COMP_URI + "all", token, "all competitions");
        String[] lines = allCompetitions.split("},");
        for (String line : lines) {
            System.out.println(line);
        }
    }

    @Test
    public void getAllCompetitionsWithRequestedCompetitorsTest() throws URISyntaxException, IOException, InterruptedException {
        String token = singInAdmin();

        String allCompetitions = get(COMP_URI + "allWithParticipants", token, "all competitions");
        String[] lines = allCompetitions.split("},");
        for (String line : lines) {
            System.out.println(line);
        }
    }

    @Test
    public void requestParticipation() throws IOException, URISyntaxException, InterruptedException {
        String token = getToken("petur_petrov@abv.bg", "Password1");
        String res = update(COMP_URI + "request/byName/Dryanovo%20cup", "", token);
        System.out.println(res);
    }

    @Test
    public void removeParticipation() throws IOException, URISyntaxException, InterruptedException {
        String token = getToken("petur_petrov@abv.bg", "Password1");
        String res = update(COMP_URI + "remove/byName/Dryanovo%20cup", "", token);
        System.out.println(res);
    }

    @Test
    public void removeClubsAndUsers() throws URISyntaxException, IOException, InterruptedException {
        String token = singInAdmin();

        removeClub("Bacho%20Kiro", token);
        removeClub("Uzana", token);
        removeClub("Valdi", token);

        removeUser("0042302211", token);
        removeUser("9042302211", token);
        removeUser("9402302211", token);
        removeUser("8002302211", token);
        removeUser("9602302211", token);
        removeUser("8402302211", token);
        removeUser("9102302211", token);
        removeUser("9612302211", token);
        removeUser("9602202211", token);

        removeCompetition("Turnovo%20cup", token);
        removeCompetition("Dryanovo%20cup", token);
        removeCompetition("Sofia%20cup", token);
    }


}