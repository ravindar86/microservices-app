package com.techie.orderservice.service;

import com.techie.orderservice.dto.InventoryResponse;
import com.techie.orderservice.dto.OrderLineItemsDto;
import com.techie.orderservice.dto.OrderRequest;
import com.techie.orderservice.model.Order;
import com.techie.orderservice.model.OrderLineItems;
import com.techie.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList =
                orderRequest
                .getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItemsList);

        // Get all skuCodes from the order line items
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(orderLineItem -> orderLineItem.getSkuCode())
                .collect(Collectors.toList());

        // Call the inventory service and get the response whether stock is there or not
        InventoryResponse[] inventoryResponseArr = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder ->
                            uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductAvailable = Arrays.stream(inventoryResponseArr)
                            .allMatch(response -> response.isInStock());

        if(allProductAvailable)
            orderRepository.save(order);
        else
            throw new IllegalArgumentException("One or More products is not in stock...");
    }

    private OrderLineItems mapToDto(OrderLineItemsDto request) {
        OrderLineItems orderLineItem = new OrderLineItems();
        orderLineItem.setSkuCode(request.getSkuCode());
        orderLineItem.setPrice(request.getPrice());
        orderLineItem.setQuantity(request.getQuantity());

        return orderLineItem;
    }
}
