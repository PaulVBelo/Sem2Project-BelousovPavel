package com.example.demo.gateway.launchtime;

import com.example.demo.models.auction.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository("auctionLaunchTimeRepository")
public interface AuctionLaunchTimeRepository extends JpaRepository<AuctionLaunchTime, Long> {
  @Query(
      "select al from AuctionLaunchTime al where al.auction.id = :auctionId"
  )
  public Optional<AuctionLaunchTime> findByAuctionId(Long auctionId);

  @Query(
      "select al.auction from AuctionLaunchTime al where al.timeTo < :threshold and al.auction.status = 'RUNNING'"
  )
  public List<Auction> findAuctionsByTimeThreshold(LocalDateTime threshold);
}
