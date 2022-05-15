package com.techie.orderservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techie.orderservice.dto.OrderLineItemsDto;
import com.techie.orderservice.dto.OrderRequest;
import com.techie.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

    @Container
    static MySQLContainer container = new MySQLContainer("mysql:latest")
            .withUsername("duke")
            .withPassword("password")
            .withDatabaseName("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository repository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    void shouldPlaceOrder() throws Exception {
        OrderRequest orderRequest = getOrderRequest();
        String requestString = objectMapper.writeValueAsString(orderRequest);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString)
        )
                .andExpect(status().isCreated());

        Assertions.assertEquals(1, repository.findAll().size());

    }

    private OrderRequest getOrderRequest() {

        OrderLineItemsDto lineItemsDto = new OrderLineItemsDto();
        lineItemsDto.setSkuCode("12345");
        lineItemsDto.setPrice(new BigDecimal(1000));
        lineItemsDto.setQuantity(1);

        return OrderRequest.builder().orderLineItemsDtoList(
                List.of(lineItemsDto)).build();
    }

}
