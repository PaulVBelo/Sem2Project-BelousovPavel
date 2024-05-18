package com.example.demo.payment.inbox;

import com.example.demo.models.auction.Status;
import com.example.demo.payment.records.PaymentTransferType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "inbox")
public class InboxRecord {
  @Id
  @Column(name = "request_id")
  private String requestId;

  @NotNull
  @Column(name="auction_id ")
  private Long auctionId;

  @DecimalMin(value = "0.0", message = "Payment cannot be below 0")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "money")
  private BigDecimal money;

  @Column(name = "p_type")
  @Enumerated(EnumType.STRING)
  private PaymentTransferType type;

  protected InboxRecord() {}

  public InboxRecord(String requestId,
                     Long auctionId,
                     BigDecimal money,
                     PaymentTransferType type) {
    this.requestId = requestId;
    this.auctionId = auctionId;
    this.money = money;
    this.type = type;
  }

  public String getRequestId() {
    return requestId;
  }

  public Long getAuctionId() {
    return auctionId;
  }

  public BigDecimal getMoney() {
    return money;
  }

  public PaymentTransferType getType() {
    return type;
  }
}
