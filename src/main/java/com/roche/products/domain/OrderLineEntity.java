package com.roche.products.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Builder
public class OrderLineEntity {

    @Id
    private String id;

    @Field("product_id")
    private String productId;

    @Field("price")
    private BigDecimal price;

    @Field("name")
    private String name;
}
