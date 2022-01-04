package com.itananina.weblamp.weblamp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itananina.weblamp.weblamp.dto.JwtRequest;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.exceptions.UserAlreadyExistsException;
import com.itananina.weblamp.weblamp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void uniqueUsernameCheck() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("user","1234","ex@mail.ru");

        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                    .content(objectMapper.writeValueAsString(jwtRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(mvcResult ->
                        Objects.requireNonNull(mvcResult.getResolvedException()).getClass().equals(UserAlreadyExistsException.class));
    }

    @Test
    public void createUserCheck() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("autotest","1234","autotestuser@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/sign-up")
                    .content(objectMapper.writeValueAsString(jwtRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> {
                    removeUser("autotest");
                });
    }

    @Transactional
    private void removeUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Test exception!"));
        userRepository.delete(user);
    }
}
