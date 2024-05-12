package com.example.demo2.exceptions;

public class BadBidRequestException extends RuntimeException{
  public BadBidRequestException(String message) {
    super(message);
  }
}
