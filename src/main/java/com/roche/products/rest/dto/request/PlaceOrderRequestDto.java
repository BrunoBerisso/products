package com.roche.products.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@Data
@Builder
@ApiModel(description = "Parameters use to place an Order.")
public class PlaceOrderRequestDto {

    @ApiModelProperty(value = "Email of the buyer")
    @JsonProperty(value = "email")
    @Email
    private String email;

    @ApiModelProperty(value = "List of products ids to add to the order")
    @JsonProperty(value = "product_ids")
    @NotEmpty
    private List<String> productsIds;
}
