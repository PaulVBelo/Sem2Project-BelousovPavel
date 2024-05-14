package com.example.demo2.models.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
  @Query(
      "select a from Auction a where a.status = :status"
  )
  public List<Auction> findByStatus(Status status);

  @Query(
      "select a from Auction a where a.originalId = :originalId and a.status != 'CANCELED'"
  )
  public Optional<Auction> findRunningByOldId(Long originalId);
}
