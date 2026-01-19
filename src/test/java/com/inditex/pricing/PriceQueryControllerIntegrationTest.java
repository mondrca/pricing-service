package com.inditex.pricing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PriceQueryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Test 1: 2020-06-14 10:00 -> price_list 1, price 35.50
    @Test
    void test1() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    // Test 2: 2020-06-14 16:00 -> price_list 2, price 25.45
    @Test
    void test2() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));
    }

    // Test 3: 2020-06-14 21:00 -> price_list 1, price 35.50
    @Test
    void test3() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("applicationDate", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    // Test 4: 2020-06-15 10:00 -> price_list 3, price 30.50
    @Test
    void test4() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("applicationDate", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(3))
                .andExpect(jsonPath("$.price").value(30.50));
    }

    // Test 5: 2020-06-16 21:00 -> price_list 4, price 38.95
    @Test
    void test5() throws Exception {
        mockMvc.perform(get("/api/v1/prices/applicable")
                        .param("applicationDate", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(4))
                .andExpect(jsonPath("$.price").value(38.95));
    }
}
