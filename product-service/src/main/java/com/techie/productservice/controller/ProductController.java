package com.techie.productservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techie.productservice.dto.ProductRequest;
import com.techie.productservice.dto.ProductResponse;
import com.techie.productservice.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    private ProductService productService;
    private ObjectMapper objectMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) throws JsonProcessingException {

        log.info("Saving product {}"+objectMapper.writeValueAsString(productRequest));
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getProducts(){
        return productService.getAllProducts();
    }
}
