package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.AbstractSpringBootTest;
import com.itananina.weblamp.weblamp.configs.SecurityConfig;
import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.exceptions.ResourceNotFoundException;
import com.itananina.weblamp.weblamp.repositories.OrderRepository;
import com.itananina.weblamp.weblamp.repositories.UserRepository;
import com.itananina.weblamp.weblamp.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

public class CartControllerTest extends AbstractSpringBootTest {
    private static final String REST_URL = "/api/v1/cart";
    private UserDetails userDetails;
    private Order mockOrder;

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityConfig securityConfig;
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
    @WithUserDetails("autotestuser")
    public void getCurrentOrderWhenAuthorizedCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    @DisabledIf("com.itananina.weblamp.weblamp.controllers.CartControllerTest#isJwtSwitchOn")
    //остальные пройдут при аутентификации через jwt токен, тк на них нет антматчера
    public void getCurrentOrderWhenUnauthorizedCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithUserDetails("autotestuser")
    public void removeProductCheck() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL+"/items/1")
                    .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(REST_URL+"/"));
    }

    @Test
    @WithUserDetails("autotestuser")
    public void removeNonExistingProductCheck() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL+"/items/5")
                    .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult ->
                        Objects.requireNonNull(mvcResult.getResolvedException()).getClass().equals(ResourceNotFoundException.class));
    }

    @Test
    @WithUserDetails("autotestuser")
    public void removeAllProductsCheck() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL+"/items")
                    .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(REST_URL+"/"));
    }

    @Test
    @WithUserDetails("autotestuser")
    public void confirmOrderCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+"/confirm"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/api/v1/orders/1"));
    }

    private User findUserByUsername() {
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()->new RuntimeException("Exception trying to find user "+userDetails.getUsername()));
    }

    private boolean isJwtSwitchOn() {
        return (boolean) ReflectionTestUtils.getField(securityConfig,"jwtSwitchOn");
    }
}
