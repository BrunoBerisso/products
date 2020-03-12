package com.roche.products.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Order definition used through the API.")
public class OrderResponseDto {

    @ApiModelProperty(value = "Id that uniquely identify an order.")
    @JsonProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "Buyer email.")
    @JsonProperty(value = "buyer_email")
    private String buyerEmail;

    @ApiModelProperty(value = "Products on this order.")
    @JsonProperty(value = "products")
    private List<ProductResponseDto> products;

    @ApiModelProperty(value = "Total price at the time of when the order was placed." +
            "This might not be equal to the sum of all the product prices if a product price change" +
            "since this order was placed, or a product was deleted.")
    @JsonProperty(value = "total")
    private BigDecimal total;

    @ApiModelProperty(value = "Date when the order was placed.")
    @JsonProperty(value = "created_date")
    private LocalDateTime createdDate;

}
