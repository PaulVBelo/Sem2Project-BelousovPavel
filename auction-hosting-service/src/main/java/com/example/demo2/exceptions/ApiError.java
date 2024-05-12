package com.example.demo2.exceptions;

public record ApiError(String message) {
  public ApiError(String message) {
    this.message = message;
  }

  public String message() {
    return this.message;
  }
}
