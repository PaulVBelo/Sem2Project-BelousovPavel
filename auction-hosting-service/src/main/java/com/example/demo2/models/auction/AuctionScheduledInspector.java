package com.example.demo2.models.auction;

import com.example.demo2.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service("auctionStopScheduler")
public class AuctionScheduledInspector {
  private final AuctionRepository auctionRepository;
  private final PaymentService paymentService;

  @Autowired
  public AuctionScheduledInspector(AuctionRepository auctionRepository,
                                   PaymentService paymentService) {
    this.auctionRepository = auctionRepository;
    this.paymentService = paymentService;
  }

  @Transactional
  @Scheduled(fixedDelay = 60000)
  public void inspectAuctions() {
    //Переводит аукционы в след. состояние в зависимости от времени (и наличия ставок)
    LocalDateTime inspectionTime = LocalDateTime.now();
    List<Auction> auctionTransferList =
        auctionRepository.findByStatusBelowTimeThreshold(Status.RUNNING, inspectionTime);
    for (Auction a: auctionTransferList) {
      if (a.getBids().isEmpty()) {
        a.setStatus(Status.AWAIT_SELFSTOP);
      } else {
        a.setStatus(Status.AWAIT_PAYMENT);
      }
      auctionRepository.save(a);
    }
    List<Auction> auctionPunishList =
        auctionRepository.findByStatusBelowTimeThreshold(Status.AWAIT_PAYMENT, inspectionTime.minusWeeks(1));
    for (Auction a: auctionPunishList) {
      a.setStatus(Status.AWAIT_SELFSTOP);
      auctionRepository.save(a);
    }
  }

  @Transactional
  @Scheduled(fixedDelay = 60000)
  public void endSelfstoppingAuctions() {
    List<Auction> toEnd = auctionRepository.findByStatus(Status.AWAIT_SELFSTOP);
    for (Auction auction: toEnd) {
      paymentService.finishAuctionByForce(auction);
    }
  }
}
