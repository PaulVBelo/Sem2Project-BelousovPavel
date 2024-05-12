package com.example.demo.models.auction;

import com.example.demo.models.auctioneer.Auctioneer;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@Entity
@Table(name = "auction")
public class Auction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "item")
  @NotBlank(message = "Item has to be named")
  private String item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "auctioneer_id")
  private Auctioneer auctioneer;

  @DecimalMin(value = "0.0", inclusive = false, message = "Step  must be more than 0")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "step")
  private BigDecimal step;

  @DecimalMin(value = "0.0", inclusive = false,
      message = "Starting price must be more than 0, since it'll be payed as penalty for auction failure.")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "start")
  private BigDecimal start;

  @Min(value = 3600,
      message = "Duration must be at least 1 hour")
  @Column(name = "duration")
  private Integer duration;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  protected Auction() {}

  public Auction(String item,
                 Auctioneer auctioneer,
                 BigDecimal step,
                 BigDecimal start,
                 Integer duration) {
    this.item = item;
    this.auctioneer = auctioneer;
    this.step = step;
    this.start = start;
    this.duration = duration;
    this.status = Status.DRAFT;
  }

  public Long getId() {
    return id;
  }

  public String getItem() {
    return item;
  }

  public Auctioneer getAuctioneer() {
    return auctioneer;
  }

  public BigDecimal getStep() {
    return step;
  }

  public BigDecimal getStart() {
    return start;
  }

  public Integer getDuration() {
    return duration;
  }

  public Status getStatus() {
    return status;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public void setAuctioneer(Auctioneer auctioneer) {
    this.auctioneer = auctioneer;
  }

  public void setStep(BigDecimal step) {
    this.step = step;
  }

  public void setStart(BigDecimal start) {
    this.start = start;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
