package com.example.demo2.management.records;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AuctionLaunchRequestDTO(
    @NotNull(message = "Internal server error. is null.") Long auctionId,
    @NotNull(message = "Internal server error. is null.") String item,
    @NotNull(message = "Internal server error. is null.") BigDecimal step,

    @NotNull(message = "Internal server error. is null.") BigDecimal start,

    @NotNull(message = "Internal server error. is null.") Integer duration
) {
}
