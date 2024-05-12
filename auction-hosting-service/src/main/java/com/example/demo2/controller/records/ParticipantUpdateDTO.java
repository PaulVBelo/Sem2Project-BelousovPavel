package com.example.demo2.controller.records;

import jakarta.validation.constraints.NotBlank;

public record ParticipantUpdateDTO(@NotBlank(message = "username has to be filled") String username) {
}
