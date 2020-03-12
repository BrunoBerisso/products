package com.roche.products.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Validated
@Data
@Builder
@ApiModel(description = "Holds the information to by updated for a Product passed though the API.")
public class UpdateProductRequestDto {

    @ApiModelProperty(value = "A new name for the Product")
    @JsonProperty(value = "name")
    @NotBlank(message = "Name can not be empty when specified")
    private String name;

    @ApiModelProperty(value = "A new price for the Product")
    @JsonProperty(value = "price")
    @NotNull(message = "Price can not be empty when specified")
    private BigDecimal price;
}
