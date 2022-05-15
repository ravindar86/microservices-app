package com.techie.orderservice.controller;

import com.techie.orderservice.dto.OrderRequest;
import com.techie.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

  /*  public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }*/

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        try{
            orderService.placeOrder(orderRequest);
            return "Order placed successfully!";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Order Not placed!! Some products are not in stock!";
        }

    }
}
