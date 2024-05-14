package com.example.demo.exceptions;

public class AuctionStopException extends RuntimeException{
  public AuctionStopException(String message) {
    super(message);
  }
}
