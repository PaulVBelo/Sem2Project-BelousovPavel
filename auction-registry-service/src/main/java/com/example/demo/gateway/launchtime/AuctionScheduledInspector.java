package com.example.demo.gateway.launchtime;

import com.example.demo.models.auction.Auction;
import com.example.demo.models.auction.AuctionRepository;
import com.example.demo.models.auction.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service("auctionStopScheduler")
public class AuctionScheduledInspector {
  private final AuctionRepository auctionRepository;
  private final AuctionLaunchTimeRepository auctionLaunchTimeRepository;

  @Autowired
  public AuctionScheduledInspector(AuctionRepository auctionRepository,
                                   AuctionLaunchTimeRepository auctionLaunchTimeRepository) {
    this.auctionRepository = auctionRepository;
    this.auctionLaunchTimeRepository = auctionLaunchTimeRepository;
  }

  @Transactional
  @Scheduled(fixedDelay = 60000)
  public void inspectAuctions() {
    List<Auction> auctionTransferList = auctionLaunchTimeRepository.findAuctionsByTimeThreshold(LocalDateTime.now());
    for (Auction a: auctionTransferList) {
      a.setStatus(Status.AWAIT_PAYMENT);
      auctionRepository.save(a);
    }
  }
}
