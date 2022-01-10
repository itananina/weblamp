package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.AbstractSpringBootTest;
import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.exceptions.ResourceNotFoundException;
import com.itananina.weblamp.weblamp.repositories.OrderRepository;
import com.itananina.weblamp.weblamp.repositories.UserRepository;
import com.itananina.weblamp.weblamp.services.UserService;
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
import java.util.Objects;
import java.util.Optional;

public class CartControllerTest extends AbstractSpringBootTest {
    private static final String REST_URL = "/api/v1/cart/";
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
        mockOrder = new Order("В процессе",findUserByUsername());
        mockOrder.setId(1l);
        mockOrder.setOrderProducts(new ArrayList<>(Arrays.asList(
                new OrderProduct(1L,new Product(1l,"Title1",50),mockOrder,100,1),
                new OrderProduct(2L,new Product(2l,"Title2",100),mockOrder,100,2))));
        Mockito.when(mockOrderRepository.findByUserIdAndStatus(findUserByUsername().getId(),"В процессе")).
                thenReturn(Optional.of(mockOrder));
    }

    @Test
    public void getCurrentOrderCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+userDetails.getUsername()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    public void removeProductCheck() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL+userDetails.getUsername()+"/items/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(REST_URL+userDetails.getUsername()));
    }


    @Test
    public void removeNonExistingProductCheck() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL+userDetails.getUsername()+"/items/5"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult ->
                        Objects.requireNonNull(mvcResult.getResolvedException()).getClass().equals(ResourceNotFoundException.class));
    }

    @Test
    public void removeAllProductsCheck() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL+userDetails.getUsername()+"/items"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(REST_URL+userDetails.getUsername()));
    }

    @Test
    public void confirmOrderCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+userDetails.getUsername()+"/confirm"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/api/v1/orders/1"));
    }

    private User findUserByUsername() {
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()->new RuntimeException("Exception trying to find user "+userDetails.getUsername()));
    }
}
