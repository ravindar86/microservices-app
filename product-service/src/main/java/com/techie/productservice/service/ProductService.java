package com.techie.productservice.service;

import com.techie.productservice.dto.ProductResponse;
import com.techie.productservice.repository.ProductRepository;
import com.techie.productservice.dto.ProductRequest;
import com.techie.productservice.model.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product p = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(p);

        log.info("Product {} is saved successfully",p.getId());
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> mapProductToResponse(product))
                .collect(Collectors.toList());
    }

    private ProductResponse mapProductToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
