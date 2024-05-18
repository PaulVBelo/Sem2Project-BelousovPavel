package com.example.demo.exceptions;

public class ResponseFailureException extends RuntimeException{
  public ResponseFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
