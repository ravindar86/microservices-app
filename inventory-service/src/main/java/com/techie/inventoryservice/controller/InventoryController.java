package com.techie.inventoryservice.controller;

import com.techie.inventoryservice.dto.InventoryResponse;
import com.techie.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    private final Environment environment;

    // http://localhost:8082/api/inventory/iphone-13,iphone13-red

    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SneakyThrows
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        System.out.println("Request reached port: "+environment.getProperty("server.port"));

        // Adding Timeout
        log.info("Wait Started");
        Thread.sleep(10000);
        log.info("Wait Ended");

        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> getAllInventory(){
        return inventoryService.getInventory();
    }
}

