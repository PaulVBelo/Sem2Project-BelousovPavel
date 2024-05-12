package com.example.demo.models.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
  @Query(
      "select a from Auction a where status = 'RUNNING'"
  )
  public List<Auction> findRunningAuctions();
}
