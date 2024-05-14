package com.example.demo2.management.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Table(name = "launch_history")
@Entity
public class LaunchHistoryRecord {
  @Id
  @Column(name = "request_id")
  private String requestId;

  @NotNull
  @Column(name = "success")
  private Boolean success;

  @NotNull
  @Column(name="og_auction_id ")
  private Long originalAuctionId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "launch_time")
  @NotNull
  private LocalDateTime launchTime;

  // Потом, может быть, сделаю очистку этой таблицы.
  // В конце концов - любая запись нужна всего 3 секунды.
  // Потом смело на сваи, пока случайно не сгенерили тот же UUID.

  protected LaunchHistoryRecord() {}

  public LaunchHistoryRecord(String requestId, Boolean success, Long originalAuctionId, LocalDateTime launchTime) {
    this.requestId = requestId;
    this.success = success;
    this.originalAuctionId = originalAuctionId;
    this.launchTime = launchTime;
  }

  public String getRequestId() {
    return requestId;
  }

  public Boolean getSuccess() {
    return success;
  }

  public Long getOriginalAuctionId() {
    return originalAuctionId;
  }

  public LocalDateTime getLaunchTime() {
    return launchTime;
  }
}
