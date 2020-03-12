package com.roche.products.rest.service;

import com.roche.products.domain.OrderEntity;
import com.roche.products.domain.ProductEntity;
import com.roche.products.domain.OrderLineEntity;
import com.roche.products.exception.ProductNotFoundException;
import com.roche.products.repository.OrderRepository;
import com.roche.products.repository.ProductRepository;
import com.roche.products.rest.dto.request.GetOrdersRequestDto;
import com.roche.products.rest.dto.request.PlaceOrderRequestDto;
import com.roche.products.rest.dto.response.OrderResponseDto;
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

    /**
     *  Return the orders placed that match the filter described in the request.
     * @param ordersFilter The filter describing the citeria to get the orders.
     * @return A list of orders
     */
    public List<OrderResponseDto> getOrders(GetOrdersRequestDto ordersFilter) {
        return orderRepository.findByCreatedDateBetween(
                ordersFilter.getFromDate(), ordersFilter.getToDate()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Place an order with the given products. The order hold a reference to the products and a copy
     * of the price and name at the moment of placing the order. Any change to the information other
     * than the price will be reflected.
     * @param orderRequest The information needed to place an order
     * @return The new order
     */
    public OrderResponseDto placeOrder(PlaceOrderRequestDto orderRequest) {
        Iterable<ProductEntity> productEntities = productRepository.findAllById(
                orderRequest.getProductsIds());

        List<OrderLineEntity> orderLines = new ArrayList<>();

        for(ProductEntity productEntity : productEntities) {
            orderLines.add(OrderLineEntity.builder()
                    .productId(productEntity.getId())
                    .price(productEntity.getPrice())
                    .name(productEntity.getName())
                    .build());
        }

        // If there is any product in the request not present in the DB throw a 404
        if (orderLines.size() < orderRequest.getProductsIds().size()) {
            throw new ProductNotFoundException();
        }

        OrderEntity orderEntity = OrderEntity.builder()
                .buyerEmail(orderRequest.getEmail())
                .products(orderLines)
                .createdDate(LocalDateTime.now())
                .build();

        orderRepository.save(orderEntity);
        return mapToDto(orderEntity);
    }

    /**
     * Build a response from a record in the DB.
     * @param entity The record in the DB
     * @return An instance of {@link OrderResponseDto}
     */
    private OrderResponseDto mapToDto(OrderEntity entity) {

        List<ProductResponseDto> products = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderLineEntity productRef : entity.getProducts()) {
            products.add(mapToDto(productRef));
            total = total.add(productRef.getPrice());
        }

        return OrderResponseDto.builder()
                .id(entity.getId())
                .buyerEmail(entity.getBuyerEmail())
                .products(products)
                .createdDate(entity.getCreatedDate())
                .total(total)
                .build();
    }

    /**
     * Utility method to create a {@link ProductResponseDto} object from a {@link OrderLineEntity}.
     * @param entity The {@link OrderLineEntity} to map
     * @return the result of mapping the {@link OrderLineEntity} to a {@link ProductResponseDto}
     */
    private ProductResponseDto mapToDto(OrderLineEntity entity) {
        return ProductResponseDto.builder()
                .sku(entity.getProductId())
                .name(entity.getName())
                .price(entity.getPrice())
                .build();
    }
}
