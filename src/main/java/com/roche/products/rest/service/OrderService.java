package com.roche.products.rest.service;

import com.roche.products.domain.OrderEntity;
import com.roche.products.domain.ProductEntity;
import com.roche.products.domain.ProductReferenceEntity;
import com.roche.products.exception.ProductNotFoundException;
import com.roche.products.repository.OrderRepository;
import com.roche.products.repository.ProductRepository;
import com.roche.products.rest.dto.GetOrdersRequestDto;
import com.roche.products.rest.dto.OrderDto;
import com.roche.products.rest.dto.PlaceOrderRequestDto;
import com.roche.products.rest.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class implementation for the CRUD API logic to manage Order entities
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public List<OrderDto> getOrders(GetOrdersRequestDto ordersFilter) {
//        return orderRepository.findByCreatedDateBetween(
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto placeOrder(PlaceOrderRequestDto orderRequest) {
        Iterable<ProductEntity> productEntities = productRepository.findAllById(
                orderRequest.getProductsIds());

        List<ProductReferenceEntity> referenceEntities = new ArrayList<>();

        for(ProductEntity productEntity : productEntities) {
            referenceEntities.add(ProductReferenceEntity.builder()
                    .productId(productEntity.getId())
                    .price(productEntity.getPrice())
                    .build());
        }

        if (referenceEntities.size() != orderRequest.getProductsIds().size()) {
            throw new ProductNotFoundException();
        }

        OrderEntity orderEntity = OrderEntity.builder()
                .buyerEmail(orderRequest.getEmail())
                .products(referenceEntities)
                .createdDate(LocalDateTime.now())
                .build();

        orderRepository.save(orderEntity);
        return mapToDto(orderEntity);
    }

    private OrderDto mapToDto(OrderEntity entity) {

        List<String> productRefIds = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ProductReferenceEntity productRef : entity.getProducts()) {
            productRefIds.add(productRef.getId());
            total = total.add(productRef.getPrice());
        }

        List<ProductDto> products = new ArrayList<>();
        Iterable<ProductEntity> productEntities = productRepository.findAllById(productRefIds);

        for(ProductEntity productEntity : productEntities) {
            products.add(mapToDto(productEntity));
        }

        return OrderDto.builder()
                .id(entity.getId())
                .buyerEmail(entity.getBuyerEmail())
                .products(products)
                .total(total)
                .build();
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
