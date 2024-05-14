package com.example.demo.gateway.records;

import jakarta.validation.constraints.NotNull;

public record AuctionStopRequestDTO(
    @NotNull(message = "Internal server error. Id is null.") Long auctionId
) {
}
