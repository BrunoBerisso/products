package com.roche.products.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Validated
@Data
@Builder
@ApiModel(description = "Parameters use to select the Orders to retrieve.")
public class GetOrdersRequestDto {

    @ApiModelProperty(value = "Filter Orders placed at this date or after.")
    @JsonProperty(value = "from_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    private LocalDateTime fromDate;

    @ApiModelProperty(value = "Filter Orders placed at this date or before.")
    @JsonProperty(value = "to_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    private LocalDateTime toDate;
}
