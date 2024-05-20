package com.example.demo2.models.bid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
  @Query(
      "select b from Bid b " +
          "where b.participant.id = :participantId and b.auction.status in ('RUNNING', 'AWAIT_PAYMENT', 'AWAIT_SELFSTOP') " +
          "and b.bidSize = (" +
          "select max(b2.bidSize) from Bid b2 where b2.auction.id = b.auction.id" +
          ")"
  )
  public List<Bid> findWinningBidsOfParticipant(Long participantId);

  @Query(
      "select b from Bid b where b.auction.id = :auctionId " +
          "and b.bidSize = (select max(b2.bidSize) from Bid b2)"
  )
  public Optional<Bid> findWinningBidForAuction(Long auctionId);
}
