package com.example.demo2.exceptions;

public class OutboxSchedulingException extends RuntimeException{
  public OutboxSchedulingException(String message, Throwable cause) {
    super(message, cause);
  }
}
