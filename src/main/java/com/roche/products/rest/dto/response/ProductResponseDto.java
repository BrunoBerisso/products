package com.roche.products.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Product definition use though the API")
public class ProductResponseDto {

    @ApiModelProperty(value = "Id that uniquely identify a product")
    @JsonProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "Product name")
    @JsonProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "Product price")
    @JsonProperty(value = "price")
    private BigDecimal price;

    @ApiModelProperty(value = "Date of creation in the database")
    @JsonProperty(value = "created_date")
    private LocalDateTime createdDate;
}
