package com.orienting.orienting.spring.test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orienting.common.dto.AuthenticationResponseDto;
import com.orienting.common.dto.UserCreationDto;
import com.orienting.common.entity.UserEntity;
import com.orienting.common.entity.UserRole;
import com.orienting.common.services.AuthenticationService;
import com.orienting.orienting.spring.controller.AuthenticationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;


    @Test
    public void testRegister() throws Exception {
        UserCreationDto user = new UserCreationDto("monika_noeva@abv.bg", "Password", "Monika", "Noeva", "0022332211", "0879856542", "W21", UserRole.COMPETITOR);
        when(authenticationService.register(ArgumentMatchers.any(UserEntity.class))).thenReturn(new AuthenticationResponseDto());

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJson))
                .andExpect(status().isOk());
    }

}