package com.roche.products.rest.controller;

import com.roche.products.rest.dto.ProductDto;
import com.roche.products.rest.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/product")
@Api(value = "CRUD operations over Products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "Return all the products present in the database")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public List<ProductDto> getAll() {
        return productService.getAll();
    }

    @ApiOperation(value = "Add a new product")
    @RequestMapping(value = "/{name}/{price}", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ProductDto addProduct(
            @ApiParam(name = "name", value = "Descriptive product name", required = true)
            @PathVariable(value = "name")
            String name,
            @ApiParam(name = "price", value = "The price of the product", required = true)
            @PathVariable(value = "price")
            BigDecimal price) {
        return productService.addProduct(name, price);
    }
}
