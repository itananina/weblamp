package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.AbstractSpringBootTest;
import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.repositories.OrderRepository;
import com.itananina.weblamp.weblamp.repositories.UserRepository;
import com.itananina.weblamp.weblamp.services.UserService;
import com.itananina.weblamp.weblamp.services.dictionaries.OrderStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class OrderDetailsControllerTest extends AbstractSpringBootTest {

    private static final String REST_URL = "/api/v1/orders";
    private UserDetails userDetails;
    private Order mockOrder;

    @Autowired
    private UserService userService;
    @MockBean
    private OrderRepository mockOrderRepository;
    @Autowired
    private UserRepository userRepository;

    @PostConstruct //выполняется перед каждым @Test
    public void init() {
        userDetails = userService.loadUserByUsername("autotestuser");
        mockOrder = new Order(OrderStatus.IN_PROCESS,findUserByUsername());
        mockOrder.setId(1l);
        mockOrder.setOrderProducts(new ArrayList<>(Arrays.asList(
                new OrderProduct(1L,new Product(1l,"Title1",50),mockOrder,100,1),
                new OrderProduct(2L,new Product(2l,"Title2",100),mockOrder,100,2))));
        Mockito.when(mockOrderRepository.findById(mockOrder.getId())).
                thenReturn(Optional.of(mockOrder));
    }

    @Test
    public void getOrderDetailsCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+"/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    private User findUserByUsername() {
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()->new RuntimeException("Exception trying to find user "+userDetails.getUsername()));
    }

}
