package com.example.demo2.management.records;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AuctionLaunchRequestDTO(
    @NotNull(message = "Internal server error. isLaunched is null.") Long auctionId,
    @NotNull(message = "Internal server error. isLaunched is null.") String item,
    @NotNull(message = "Internal server error. isLaunched is null.") BigDecimal step,

    @NotNull(message = "Internal server error. isLaunched is null.") BigDecimal start,

    @NotNull(message = "Internal server error. isLaunched is null.") Integer duration
) {
}
