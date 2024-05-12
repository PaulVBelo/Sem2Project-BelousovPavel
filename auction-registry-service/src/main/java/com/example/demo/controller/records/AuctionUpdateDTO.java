package com.example.demo.controller.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AuctionUpdateDTO(
    @NotBlank(message = "Item has to be named") String item,
    @DecimalMin(value = "0.0", inclusive = false, message = "Step  must be more than 0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal step,

    @DecimalMin(value = "0.0", inclusive = false,
        message = "Starting price must be more than 0, since it'll be payed as penalty for auction failure.")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal start,

    @Min(value = 600) @Max(value = 7776000) Integer duration
) {
}
