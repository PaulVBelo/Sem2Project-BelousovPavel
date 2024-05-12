package com.example.demo.models.auctioneer;

import com.example.demo.models.auction.Auction;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Table(name = "auctioneer")
public class Auctioneer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "username")
  @NotBlank(message = "username has to be filled")
  private String username;

  @DecimalMin(value = "0.0", message = "auctioneer's funds cannot be below 0")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "money")
  private BigDecimal money;

  @OneToMany(mappedBy = "auctioneer", orphanRemoval = true, cascade = PERSIST)
  private List<Auction> auctions = new ArrayList<>();

  protected Auctioneer() {}

  public Auctioneer(String username) {
    this.username = username;
    this.money = BigDecimal.valueOf(0);
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

  public List<Auction> getAuctions() {
    return auctions;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setMoney(BigDecimal money) {
    this.money = money;
  }
}
