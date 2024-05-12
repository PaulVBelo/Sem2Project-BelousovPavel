package com.example.demo2.models.auction;

import com.example.demo2.models.bid.Bid;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

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

  @DecimalMin(value = "0.0", inclusive = false, message = "Step  must be more than 0")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "step")
  private BigDecimal step;

  @DecimalMin(value = "0.0", inclusive = false,
      message = "Starting price must be more than 0, since it'll be payed as penalty for auction failure.")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "start")
  private BigDecimal start;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "time_from")
  @NotNull
  private Date timeFrom;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "time_to")
  @NotNull
  private Date timeTo;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "og_id")
  @NotNull
  private Long originalId;

  @OneToMany(mappedBy = "auction", orphanRemoval = true, cascade = PERSIST)
  private List<Bid> bids = new ArrayList<>();

  protected Auction() {}

  public Auction(String item,
                 BigDecimal step,
                 BigDecimal start,
                 Date timeFrom,
                 Date timeTo) {
    this.item = item;
    this.step = step;
    this.start = start;
    this.timeFrom = timeFrom;
    this.timeTo = timeTo;
    this.status = Status.RUNNING;
  }

  public Long getId() {
    return id;
  }

  public String getItem() {
    return item;
  }

  public BigDecimal getStep() {
    return step;
  }

  public BigDecimal getStart() {
    return start;
  }

  public Date getTimeFrom() {
    return timeFrom;
  }

  public Date getTimeTo() {
    return timeTo;
  }

  public Status getStatus() {
    return status;
  }

  public Long getOriginalId() {
    return originalId;
  }

  public List<Bid> getBids() {
    return bids;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
