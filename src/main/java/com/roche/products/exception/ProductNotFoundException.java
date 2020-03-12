package com.roche.products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The provided SKU could not be found")
public class ProductNotFoundException extends RuntimeException {
}
