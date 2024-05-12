package com.example.demo2.models.participant;

import com.example.demo2.models.bid.Bid;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Table(name = "participant")
public class Participant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username")
  @NotBlank(message = "username has to be filled")
  private String username;

  @DecimalMin(value = "0.0", message = "participant's funds cannot be below 0")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "money")
  private BigDecimal money;

  @OneToMany(mappedBy = "participant", orphanRemoval = true, cascade = PERSIST)
  private List<Bid> auctions = new ArrayList<>();

  protected Participant() {};

  public Participant(String username, BigDecimal money) {
    this.username = username;
    this.money = money;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public BigDecimal getMoney() {
    return money;
  }

  public List<Bid> getAuctions() {
    return auctions;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setMoney(BigDecimal money) {
    this.money = money;
  }
}
