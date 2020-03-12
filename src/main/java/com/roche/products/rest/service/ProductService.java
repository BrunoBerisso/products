package com.roche.products.rest.service;

import com.roche.products.domain.ProductEntity;
import com.roche.products.repository.ProductRepository;
import com.roche.products.rest.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDto> getAll() {
        return productRepository.findAllNotDeleted().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ProductDto addProduct(String name, BigDecimal price) {
        ProductEntity entity = ProductEntity.builder()
                .name(name)
                .price(price)
                .createdDate(LocalDateTime.now())
                .build();

        entity = productRepository.insert(entity);
        return mapToDto(entity);
    }

    private ProductDto mapToDto(ProductEntity entity) {
        return ProductDto.builder()
                .sku(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .createdDate(entity.getCreatedDate())
                .build();
    }

}
