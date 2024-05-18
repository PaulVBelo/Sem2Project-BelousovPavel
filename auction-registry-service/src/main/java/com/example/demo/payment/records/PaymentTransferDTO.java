package com.example.demo.payment.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentTransferDTO(
    @NotNull String requestId,
    @NotNull Long auctionId,
    @DecimalMin(value = "0.0", message = "participant's funds cannot be below 0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal money,
    @NotNull PaymentTransferType type
    ) {
}
