package com.example.demo.gateway.records;

import java.math.BigDecimal;

public record AuctionLaunchRequestDTO(
    Long auctionId,
    String item,
    BigDecimal step,

    BigDecimal start,

    Integer duration
) {
}
