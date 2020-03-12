package com.roche.products.rest.service;

import com.roche.products.domain.OrderEntity;
import com.roche.products.domain.ProductEntity;
import com.roche.products.domain.ProductReferenceEntity;
import com.roche.products.exception.ProductNotFoundException;
import com.roche.products.repository.OrderRepository;
import com.roche.products.repository.ProductRepository;
import com.roche.products.rest.dto.request.GetOrdersRequestDto;
import com.roche.products.rest.dto.response.OrderResponseDto;
import com.roche.products.rest.dto.request.PlaceOrderRequestDto;
import com.roche.products.rest.dto.response.ProductResponseDto;
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

    public List<OrderResponseDto> getOrders(GetOrdersRequestDto ordersFilter) {
        //FIXME: date filtering needs some special attention for MongoDB
//        return orderRepository.findByCreatedDateBetween(
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderResponseDto placeOrder(PlaceOrderRequestDto orderRequest) {
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

    private OrderResponseDto mapToDto(OrderEntity entity) {

        List<String> productRefIds = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ProductReferenceEntity productRef : entity.getProducts()) {
            productRefIds.add(productRef.getId());
            total = total.add(productRef.getPrice());
        }

        List<ProductResponseDto> products = new ArrayList<>();
        Iterable<ProductEntity> productEntities = productRepository.findAllById(productRefIds);

        for(ProductEntity productEntity : productEntities) {
            products.add(mapToDto(productEntity));
        }

        return OrderResponseDto.builder()
                .id(entity.getId())
                .buyerEmail(entity.getBuyerEmail())
                .products(products)
                .total(total)
                .build();
    }

    /**
     * Utility method to create a {@link ProductResponseDto} object from a {@link ProductEntity}.
     * @param entity The {@link ProductEntity} to map
     * @return the result of mapping the {@link ProductEntity} to a {@link ProductResponseDto}
     */
    private ProductResponseDto mapToDto(ProductEntity entity) {
        return ProductResponseDto.builder()
                .sku(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}
