package com.itananina.weblamp.weblamp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itananina.weblamp.weblamp.AbstractSpringBootTest;
import com.itananina.weblamp.weblamp.configs.SecurityConfig;
import com.itananina.weblamp.weblamp.dto.JwtRequest;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.exceptions.IncorrectUsernameOrPasswordException;
import com.itananina.weblamp.weblamp.exceptions.UserAlreadyExistsException;
import com.itananina.weblamp.weblamp.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.PostConstruct;
import java.util.Objects;


public class AuthControllerTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void authCheck() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("autotestuser","1234");

        perform(MockMvcRequestBuilders.post("/auth")
                    .content(objectMapper.writeValueAsString(jwtRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void incorrectUsernameCheck() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("_autotestuser","1234");

        perform(MockMvcRequestBuilders.post("/auth")
                .content(objectMapper.writeValueAsString(jwtRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult ->
                        Objects.requireNonNull(mvcResult.getResolvedException()).getClass().equals(IncorrectUsernameOrPasswordException.class));
    }

    @Test
    public void incorrectPasswordCheck() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("autotestuser","1111");

        perform(MockMvcRequestBuilders.post("/auth")
                .content(objectMapper.writeValueAsString(jwtRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult ->
                        Objects.requireNonNull(mvcResult.getResolvedException()).getClass().equals(IncorrectUsernameOrPasswordException.class));
    }

    @Test
    public void createUserUniqueUsernameCheck() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("user","1234");

        perform(MockMvcRequestBuilders.post("/sign-up")
                    .content(objectMapper.writeValueAsString(jwtRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(mvcResult ->
                        Objects.requireNonNull(mvcResult.getResolvedException()).getClass().equals(UserAlreadyExistsException.class));
    }

    @Test
    public void createUserCheck() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("autotest_signup","1234","autotest_signup@mail.ru");

        perform(MockMvcRequestBuilders.post("/sign-up")
                    .content(objectMapper.writeValueAsString(jwtRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty())
                .andExpect(mvcResult -> {
                    removeUser("autotest_signup");
                });
    }

    private void removeUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("Exception trying to find user "+username));
        userRepository.delete(user);
    }
}
