package com.example.demo2.controller.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record BidRequestDTO(
    @NotNull Long participantId,
    @DecimalMin(value = "0.0", inclusive = false, message = "Bid must be more than 0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal bidSize
    ) {
}
