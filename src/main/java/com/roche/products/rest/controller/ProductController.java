package com.roche.products.rest.controller;

import com.roche.products.domain.ProductEntity;
import com.roche.products.repository.ProductRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/product")
@Api(value = "CRUD operations over Products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @ApiOperation(value = "Return all the products present in the database")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public List<ProductEntity> getAll() {
        return productRepository.findAll();
    }
}
