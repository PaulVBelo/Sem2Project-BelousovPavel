package com.example.demo.controller.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AuctionResponseDTO(String item,
                                 String auctioneerName,
                                 BigDecimal step,

                                 BigDecimal start,

                                Integer duration) {
}
