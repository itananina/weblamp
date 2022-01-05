package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.AbstractSpringBootTest;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.repositories.OrderRepository;
import com.itananina.weblamp.weblamp.repositories.UserRepository;
import com.itananina.weblamp.weblamp.services.JwtTokenUtil;
import com.itananina.weblamp.weblamp.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class OrdersControllerTest extends AbstractSpringBootTest {

    private static final String REST_URL = "/api/v1/orders";
    private UserDetails userDetails;
    private String token;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void init() {
        userDetails = userService.loadUserByUsername("autotestuser");
        token = jwtTokenUtil.generateToken(userDetails);
    }

    @Test
    public void getCurrentOrderWhenAuthorizedCheck() throws Exception {
        Assert.assertNotNull(token);
        perform(MockMvcRequestBuilders.get(REST_URL+"/active")
                    .header("Authorization", "Bearer " + token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getCurrentOrderWhenUnauthorizedCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+"/active"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void addNewProductCheck() throws Exception {
        Assert.assertNotNull(token);
        perform(MockMvcRequestBuilders.get(REST_URL+"/items/1")
                    .header("Authorization", "Bearer " + token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productList[0]").isNotEmpty());
    }

    @PreDestroy
    private void removeOrderByUsername() {
        orderRepository.deleteByUserId(findUserByUsername().getId());
    }

    private User findUserByUsername() {
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()->new RuntimeException("Exception trying to find user "+userDetails.getUsername()));
    }
}
