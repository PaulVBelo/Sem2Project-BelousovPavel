package com.example.demo2.models.bid;

import com.example.demo2.models.auction.Auction;
import com.example.demo2.models.participant.Participant;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

@Entity
@Table(name = "bid")
public class Bid {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @DecimalMin(value = "0.0", inclusive = false, message = "Bid must be more than 0")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "bid_size")
  private BigDecimal bidSize;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "auction_id")
  private Auction auction;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "participant_id")
  private Participant participant;

  protected Bid() {}

  public Bid(BigDecimal bidSize, Auction auction, Participant participant) {
    this.bidSize = bidSize;
    this.auction = auction;
    this.participant = participant;
  }

  public Long getId() {
    return id;
  }

  public BigDecimal getBidSize() {
    return bidSize;
  }

  public Auction getAuction() {
    return auction;
  }

  public Participant getParticipant() {
    return participant;
  }

  public void setBidSize(BigDecimal bidSize) {
    this.bidSize = bidSize;
  }

  public void setAuction(Auction auction) {
    this.auction = auction;
  }

  public void setParticipant(Participant participant) {
    this.participant = participant;
  }
}
