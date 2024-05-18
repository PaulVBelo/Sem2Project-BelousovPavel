package com.example.demo2.payment.inbox;


import com.example.demo2.payment.records.PaymentTransferType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "inbox")
public class InboxRecord {
  @Id
  @Column(name = "response_id")
  private String responseId;

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

  public InboxRecord(String responseId,
                     Long auctionId,
                     BigDecimal money,
                     PaymentTransferType type) {
    this.responseId = responseId;
    this.auctionId = auctionId;
    this.money = money;
    this.type = type;
  }

  public String getResponseId() {
    return responseId;
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
