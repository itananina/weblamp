package com.itananina.weblamp.weblamp.controllers;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest //tells Spring Boot to look for a @SpringBootApplication and use that to start a Spring application context.
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@WebMvcTest //создаст только бин контроллера, а инжекции создавать не будет
public class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc; //позволяет тестировать контроллеры без запуска http-сервера, при выполнении тестов сетевое соединение не создается
    private static Integer totalElements = 0;

    @Test
    @Order(1)
    void pageSizeCheck() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value("4"))
                .andDo(mvcResult -> {
                    String response = mvcResult.getResponse().getContentAsString();
                    totalElements = JsonPath.parse(response).read("$.totalElements");
                });
    }

    @Test
    void pageNumberBelowZeroCheck() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products?page=-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value("0"));
    }

    @Test
    void maxFilterBelowZeroCheck() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products?max_price=-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty());
    }

    @Test
    void minFilterAtMaxValueCheck() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products?min_price=1000000000"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty());
    }

    @Test
    @Order(2)
    void titlePartFilterCheck() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products?title_part=о"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[?(@.totalElements < "+totalElements+")]").exists());
    }

}
