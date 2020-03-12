package com.roche.products.rest.service;

import com.roche.products.domain.ProductEntity;
import com.roche.products.exception.ProductNotFoundException;
import com.roche.products.repository.ProductRepository;
import com.roche.products.rest.dto.ProductDto;
import com.roche.products.rest.dto.UpdateProductRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class implementation for the CRUD API logic to manage Product entities
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
        return productRepository.findByIsDeletedFalse().stream()
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
     * Update the information specified by the object {@link UpdateProductRequestDto} on the Product
     * with the given SKU
     *
     * @param sku The SKU (unique id) of the Product to update
     * @param requestDto The requested fields to upde
     * @return The modified Product saved in the database
     * @throws ProductNotFoundException If the SKU isn't found in the database
     */
    public ProductDto updateProduct(String sku, UpdateProductRequestDto requestDto) throws ProductNotFoundException {
        ProductEntity productEntity = productRepository.findById(sku)
                .orElseThrow(ProductNotFoundException::new);

        if (requestDto.getName() != null) {
            productEntity.setName(requestDto.getName());
        }

        if (requestDto.getPrice() != null) {
            productEntity.setPrice(requestDto.getPrice());
        }

        productRepository.save(productEntity);
        return mapToDto(productEntity);
    }

    /**
     * Mark a Product as deleted (soft delete)
     * @param sku The unique id of the Product to delete
     * @return The deleted Product saved in the database
     * @throws ProductNotFoundException If the SKU isn't found in the database
     */
    public ProductDto deleteProduct(String sku) throws ProductNotFoundException {
        ProductEntity productEntity = productRepository.findById(sku)
                .orElseThrow(ProductNotFoundException::new);

        productEntity.setIsDeleted(true);
        productRepository.save(productEntity);

        return mapToDto(productEntity);
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
