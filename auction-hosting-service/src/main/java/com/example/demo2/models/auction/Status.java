package com.example.demo2.models.auction;

public enum Status {
  RUNNING, AWAIT_PAYMENT, PAYMENT_PENDING, AWAIT_SELFSTOP, SELFSTOP_PENDING, CLOSED, CANCELED;

  Status() {}
}
