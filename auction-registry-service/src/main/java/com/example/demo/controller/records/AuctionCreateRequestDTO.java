package com.example.demo.controller.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AuctionCreateRequestDTO(
    @NotBlank(message = "Item has to be named") String item,
    @NotNull(message = "Referenced auctioneer Id has to be not null") Long auctioneerId,
    @DecimalMin(value = "0.0", inclusive = false, message = "Step  must be more than 0")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal step,

    @DecimalMin(value = "0.0", inclusive = false,
        message = "Starting price must be more than 0, since it'll be payed as penalty for auction failure.")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal start,

    // Самый простой способ сохранить время. Потом используется для окончательного расчёта дат при запуске.
    // Время записано в секундах. Минимум 10 минут, максимум 90 дней.
    // UI не будет, но условимся, что перевод запроса в секунды происходит на JavaScript до fetch(method = POST, ...)
    @Min(value = 600) @Max(value = 7776000) Integer duration
) {
}
