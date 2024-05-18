package com.example.demo2.models.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
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
  public Optional<Auction> findActiveByOldId(Long originalId);

  @Query(
      "select a from Auction a where a.status = :status and a.timeTo > :threshold"
  )
  public List<Auction> findByStatusAboveTimeThreshold(Status status, LocalDateTime threshold);

  @Query(
      "select a from Auction a where a.status = :status and a.timeTo < :threshold"
  )
  public List<Auction> findByStatusBelowTimeThreshold(Status status, LocalDateTime threshold);
}
