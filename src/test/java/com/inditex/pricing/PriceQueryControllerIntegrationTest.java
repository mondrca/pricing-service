package com.inditex.pricing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PriceQueryControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  // Test 1: 2020-06-14 10:00 -> price_list 1, price 35.50
  @Test
  void test1() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/prices/applicable")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(1))
        .andExpect(jsonPath("$.price").value(35.50))
        .andExpect(jsonPath("$.currency").value("EUR"))
        .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
        .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"));
  }

  // Test 2: 2020-06-14 16:00 -> price_list 2, price 25.45
  @Test
  void test2() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/prices/applicable")
                .param("applicationDate", "2020-06-14T16:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(2))
        .andExpect(jsonPath("$.price").value(25.45))
        .andExpect(jsonPath("$.currency").value("EUR"))
        .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
        .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00"));
  }

  // Test 3: 2020-06-14 21:00 -> price_list 1, price 35.50
  @Test
  void test3() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/prices/applicable")
                .param("applicationDate", "2020-06-14T21:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(1))
        .andExpect(jsonPath("$.price").value(35.50))
        .andExpect(jsonPath("$.currency").value("EUR"))
        .andExpect(jsonPath("$.startDate").value("2020-06-14T00:00:00"))
        .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"));
  }

  // Test 4: 2020-06-15 10:00 -> price_list 3, price 30.50
  @Test
  void test4() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/prices/applicable")
                .param("applicationDate", "2020-06-15T10:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(3))
        .andExpect(jsonPath("$.price").value(30.50))
        .andExpect(jsonPath("$.currency").value("EUR"))
        .andExpect(jsonPath("$.startDate").value("2020-06-15T00:00:00"))
        .andExpect(jsonPath("$.endDate").value("2020-06-15T11:00:00"));
  }

  // Test 5: 2020-06-16 21:00 -> price_list 4, price 38.95
  @Test
  void test5() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/prices/applicable")
                .param("applicationDate", "2020-06-16T21:00:00")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.productId").value(35455))
        .andExpect(jsonPath("$.brandId").value(1))
        .andExpect(jsonPath("$.priceList").value(4))
        .andExpect(jsonPath("$.price").value(38.95))
        .andExpect(jsonPath("$.currency").value("EUR"))
        .andExpect(jsonPath("$.startDate").value("2020-06-15T16:00:00"))
        .andExpect(jsonPath("$.endDate").value("2020-12-31T23:59:59"));
  }

  // Negative: missing applicationDate -> 400
  @Test
  void shouldReturn400WhenApplicationDateMissing() throws Exception {
    mockMvc
        .perform(get("/api/v1/prices/applicable").param("productId", "35455").param("brandId", "1"))
        .andExpect(status().isBadRequest());
  }

  // Negative: invalid applicationDate -> 400
  @Test
  void shouldReturn400WhenApplicationDateInvalid() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/prices/applicable")
                .param("applicationDate", "invalid-date")
                .param("productId", "35455")
                .param("brandId", "1"))
        .andExpect(status().isBadRequest());
  }

  // Negative: no applicable price -> 404
  @Test
  void shouldReturn404WhenNoApplicablePriceFound() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/prices/applicable")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "99999")
                .param("brandId", "1"))
        .andExpect(status().isNotFound());
  }

  // Negative: invalid productId (not positive) -> 400
  @Test
  void shouldReturn400WhenProductIdNotPositive() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/prices/applicable")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "0")
                .param("brandId", "1"))
        .andExpect(status().isBadRequest());
  }
}
