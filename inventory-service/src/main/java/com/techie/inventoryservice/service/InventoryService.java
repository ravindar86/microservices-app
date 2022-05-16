package com.techie.inventoryservice.service;

import com.techie.inventoryservice.dto.InventoryResponse;
import com.techie.inventoryservice.model.Inventory;
import com.techie.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> getInventoryResponse(inventory))
                .collect(Collectors.toList());
    }

    private InventoryResponse getInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity() > 0)
                .build();
    }

    public List<InventoryResponse> getInventory(){
        return inventoryRepository.findAll()
                .stream()
                .map(inventory -> getInventoryResponse(inventory))
                .collect(Collectors.toList());
    }
}
