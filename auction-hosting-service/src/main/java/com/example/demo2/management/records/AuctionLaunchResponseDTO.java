package com.example.demo2.management.records;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AuctionLaunchResponseDTO(
    @NotNull(message = "Internal server error. isLaunched is null.") Boolean isLaunched,
    @NotNull(message = "Internal server error. Id is null.") Long auctionId,
    @NotNull(message = "Internal server error. LaunchTime is null.") LocalDateTime launchTime
    ) {
}
