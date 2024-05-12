package com.example.demo.controller.records;

import java.math.BigDecimal;

public record AuctioneerResponseDTO(
    Long id,
    String username,
    BigDecimal money
) {
}
