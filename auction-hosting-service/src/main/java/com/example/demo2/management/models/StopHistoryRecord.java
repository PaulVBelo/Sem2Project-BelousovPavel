package com.example.demo2.management.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Table(name = "stop_history")
@Entity
public class StopHistoryRecord {
  @Id
  @Column(name = "request_id")
  private String requestId;

  @NotNull
  @Column(name = "success")
  private Boolean success;

  protected StopHistoryRecord() {}

  public StopHistoryRecord(String requestId, Boolean success) {
    this.requestId = requestId;
    this.success = success;
  }

  public String getRequestId() {
    return requestId;
  }

  public Boolean getSuccess() {
    return success;
  }
}
