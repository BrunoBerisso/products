package com.roche.products.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "product")
public class ProductEntity {

    @Id
    private String sku;

    @Field("name")
    private String name;

    @Field("price")
    private BigDecimal price;

    @Field("created")
    private LocalDateTime createdDate;
}
