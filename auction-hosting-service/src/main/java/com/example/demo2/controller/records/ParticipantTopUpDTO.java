package com.example.demo2.controller.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record ParticipantTopUpDTO(
    @DecimalMin(value = "0.0", message = "participant cannot top up none or negative amount of money")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal topUp
    ) {
}
