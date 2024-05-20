package com.example.demo.gateway.launchtime;

import com.example.demo.models.auction.Auction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "launchtime")
public class AuctionLaunchTime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long recordId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "auction_id")
  private Auction auction;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "time_from")
  @NotNull
  private LocalDateTime timeFrom;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "time_to")
  @NotNull
  private LocalDateTime timeTo;

  protected AuctionLaunchTime() {}

  public AuctionLaunchTime(Auction auction, LocalDateTime timeFrom, LocalDateTime timeTo) {
    this.auction = auction;
    this.timeFrom = timeFrom;
    this.timeTo = timeTo;
  }

  public Long getRecordId() {
    return recordId;
  }

  public Auction getAuction() {
    return auction;
  }

  public LocalDateTime getTimeFrom() {
    return timeFrom;
  }

  public LocalDateTime getTimeTo() {
    return timeTo;
  }
}
