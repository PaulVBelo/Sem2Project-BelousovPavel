package com.example.demo2.exceptions;

public class PaymentException extends RuntimeException{
  public PaymentException(String message) {
    super(message);
  }
}
