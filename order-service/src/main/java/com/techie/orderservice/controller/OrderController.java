package com.techie.orderservice.controller;

import com.techie.orderservice.dto.OrderRequest;
import com.techie.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name="inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
      //  try{
            return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
      /*  }
        catch (Exception e) {
            e.printStackTrace();
            return "Order Not placed!! Some products are not in stock!";
        } */
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest req, RuntimeException ex){
        return CompletableFuture.supplyAsync(() -> "Exception occurred while placing order!");
    }
}
