package com.example.demo.controller.records;

import jakarta.validation.constraints.NotBlank;

public record AuctioneerUpdateDTO(@NotBlank(message = "username has to be filled")String username) {
}
