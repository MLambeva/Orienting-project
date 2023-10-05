package com.orienting.orienting.spring.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orienting.common.entity.ClubEntity;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.entity.UserRole;
import com.orienting.common.services.UserService;
import com.orienting.common.utils.JwtUtils;
import com.orienting.orienting.spring.controller.UserController;
import lombok.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.delete;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<UserEntity> users = new ArrayList<>();

    private UserEntity savedUser;

    @BeforeEach
    public void setUp() {
        ClubEntity club1 = new ClubEntity("Bacho Kiro", "Dryanovo");
        ClubEntity club2 = new ClubEntity("Uzana", "Gabrovo");
        this.users.add(new UserEntity(0, "admin@abv.bg", "Password1", "Admin", "Admin", "0042102211", "", "", UserRole.ADMIN, null));
        this.users.add(new UserEntity(1, "monika_noeva@abv.bg", "Password1", "Monika", "Noeva", "0042302211", "0879856542", "W21", UserRole.COMPETITOR, club1));
        this.users.add(new UserEntity(2, "monika_noeva1@abv.bg", "Password1", "Monika", "Noeva", "0142302211", "0879856642", "W21", UserRole.COMPETITOR, club1));
        this.users.add(new UserEntity(3, "monika_noeva2@abv.bg", "Password1", "Monika", "Noeva", "0242302211", "0879856742", "W21", UserRole.COACH, club1));
        this.users.add(new UserEntity(4, "monika_noeva3@abv.bg", "Password1", "Monika", "Noeva", "0342302211", "0879856742", "W21", UserRole.COACH, club1));
        this.users.add(new UserEntity(5, "monika_noeva4@abv.bg", "Password1", "Monika", "Noeva", "0442302211", "0879856742", "W21", UserRole.COMPETITOR, club1));
        this.users.add(new UserEntity(6, "monika_noeva5@abv.bg", "Password1", "Monika", "Noeva", "0542302211", "0879856742", "W21", UserRole.COMPETITOR, club2));
        this.users.add(new UserEntity(7, "monika_noeva6@abv.bg", "Password1", "Monika", "Noeva", "0642302211", "0879856742", "W21", UserRole.COMPETITOR, club2));
        this.users.add(new UserEntity(8, "monika_noeva7@abv.bg", "Password1", "Monika", "Noeva", "0742302211", "0879856742", "W21", UserRole.COMPETITOR, club2));
        this.users.add(new UserEntity(9, "monika_noeva8@abv.bg", "Password1", "Monika", "Noeva", "0842302211", "0879856742", "W21", UserRole.COMPETITOR, club2));
        this.users.add(new UserEntity(10, "monika_noeva9@abv.bg", "Password1", "Monika", "Noeva", "0942302211", "0879856742", "W21", UserRole.COACH, club2));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testGetAllUsers() throws Exception {
        given(userService.getUsers()).willReturn(this.users);

        this.mockMvc.perform(get("/api/users/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(users.size())));
    }

    @Test
    @WithMockUser(authorities = {"COMPETITOR, COACH"})
    public void testGetAllUsersFail() throws Exception {
        given(userService.getUsers()).willReturn(this.users);

        this.mockMvc.perform(get("/api/users/all")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testGetUserByUcn() throws Exception {
        given(userService.getUserByUcn("0042302211")).willReturn(users.get(1));

        this.mockMvc.perform(get("/api/users/byUcn/0042302211")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }
}

