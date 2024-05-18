package com.example.demo2.payment.records;

import jakarta.validation.constraints.NotNull;

public record PaymentRequestDTO(@NotNull Long auctionId,
                                @NotNull Long participantId) {
}
