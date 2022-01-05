package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.AbstractSpringBootTest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductsControllerTest extends AbstractSpringBootTest {

    private static Integer totalElements = 0;
    private static final String REST_URL = "/api/v1/products";

    @Test
    @Order(1)
    void pageSizeCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value("4"))
                .andDo(mvcResult -> {
                    String response = mvcResult.getResponse().getContentAsString();
                    totalElements = JsonPath.parse(response).read("$.totalElements");
                });
    }

    @Test
    void pageNumberBelowZeroCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+"?page=-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value("0"));
    }

    @Test
    void maxFilterBelowZeroCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+"?max_price=-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty());
    }

    @Test
    void minFilterAtMaxValueCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+"?min_price=1000000000"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isEmpty());
    }

    @Test
    @Order(2)
    void titlePartFilterCheck() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+"?title_part=о"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[?(@.totalElements < "+totalElements+")]").exists());
    }

}
