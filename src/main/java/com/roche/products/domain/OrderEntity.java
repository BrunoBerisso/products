package com.roche.products.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "order")
public class OrderEntity {

    @Id
    private String id;

    @Field("buyer_email")
    private String buyerEmail;

    @Field("created")
    private LocalDateTime createdDate;

    @Field("products")
    private List<ProductReferenceEntity> products;
}
