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

/**
 * Service class implementing the CRUD API logic to manage Product entities
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * This method finds all Products in the database that are not deleted (deleted: false)
     * @return a list of {@link ProductDto} objects
     */
    public List<ProductDto> getAll() {
        return productRepository.findAllNotDeleted().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Add a product to the database. The creation date is automatically set to the current time
     * without any time zone information. The SKU is also created and returned in the resulting
     * object.
     *
     * @param name A String to be used as Product name
     * @param price A BigDecimal number to set as the price
     *
     * @return the newly created {@link ProductDto} object
     */
    public ProductDto addProduct(String name, BigDecimal price) {
        ProductEntity entity = ProductEntity.builder()
                .name(name)
                .price(price)
                .createdDate(LocalDateTime.now())
                .build();

        entity = productRepository.insert(entity);
        return mapToDto(entity);
    }

    /**
     * Utility method to create a {@link ProductDto} object from a {@link ProductEntity}.
     * @param entity The {@link ProductEntity} to map
     * @return the result of mapping the {@link ProductEntity} to a {@link ProductDto}
     */
    private ProductDto mapToDto(ProductEntity entity) {
        return ProductDto.builder()
                .sku(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .createdDate(entity.getCreatedDate())
                .build();
    }

}
