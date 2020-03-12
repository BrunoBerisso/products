package com.roche.products.rest.controller;

import com.roche.products.rest.dto.GetOrdersRequestDto;
import com.roche.products.rest.dto.OrderDto;
import com.roche.products.rest.dto.PlaceOrderRequestDto;
import com.roche.products.rest.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/order")
@Api(value = "CRUD operations over Orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @ApiOperation(value = "Retrieve all Orders in the given time period")
    @RequestMapping(value = "/find", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<OrderDto> getOrders(
            @ApiParam(name = "ordersFilter", value = "The criteria used to filter the orders", required = true)
            @RequestBody(required = true)
            @Validated
            GetOrdersRequestDto ordersFilter) {
        return orderService.getOrders(ordersFilter);
    }

    @ApiOperation(value = "Place a new Order with the given products")
    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public OrderDto placeOrder(
            @ApiParam(name = "orderRequest", value = "The details about the new order", required = true)
            @RequestBody(required = true)
            @Validated
            PlaceOrderRequestDto orderRequest) {
        return orderService.placeOrder(orderRequest);
    }
}
