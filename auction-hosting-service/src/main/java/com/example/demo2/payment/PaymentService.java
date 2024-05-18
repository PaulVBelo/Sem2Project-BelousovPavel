package com.example.demo2.payment;

import com.example.demo2.exceptions.CredibilityNotVerifiedException;
import com.example.demo2.exceptions.PaymentException;
import com.example.demo2.exceptions.OutboxSchedulingException;
import com.example.demo2.models.auction.Auction;
import com.example.demo2.models.auction.AuctionRepository;
import com.example.demo2.models.auction.Status;
import com.example.demo2.models.bid.Bid;
import com.example.demo2.models.bid.BidRepository;
import com.example.demo2.outbox.OutboxRecord;
import com.example.demo2.outbox.OutboxRepository;
import com.example.demo2.payment.records.PaymentRequestDTO;
import com.example.demo2.payment.records.PaymentTransferDTO;
import com.example.demo2.payment.records.PaymentTransferType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("paymentService")
public class PaymentService {
  private final OutboxRepository outboxRepository;
  private final AuctionRepository auctionRepository;
  private final BidRepository bidRepository;
  private final ObjectMapper objectMapper;

  public PaymentService(OutboxRepository outboxRepository,
                        AuctionRepository auctionRepository,
                        BidRepository bidRepository,
                        ObjectMapper objectMapper) {
    this.outboxRepository = outboxRepository;
    this.auctionRepository = auctionRepository;
    this.bidRepository = bidRepository;
    this.objectMapper = objectMapper;
  }

  @Transactional
  public void prepareAuctionForPayment(PaymentRequestDTO requestDTO, Boolean full) {
    Auction auction = auctionRepository.findById(requestDTO.auctionId()).orElseThrow();
    if (auction.getStatus() == Status.PAYMENT_PENDING) {
      throw new PaymentException("Payment is already in order. Please wait!");
    }
    Bid bid = bidRepository.findWinningBidForAuction(auction.getId()).orElseThrow();
    if (bid.getParticipant().getId() != requestDTO.participantId()) {
      throw new PaymentException("Paying participant is not the winner");
    } else {
      // To Outbox
      if (full) {
        List<Bid> winningBidsOfParticipant = bidRepository.findWinningBidsOfParticipant(bid.getParticipant().getId());
        BigDecimal sum = BigDecimal.valueOf(0);
        for (Bid b: winningBidsOfParticipant) {sum.add(b.getAuction().getStart());}
        sum.subtract(bid.getAuction().getStart()).add(bid.getBidSize());
        if (sum.compareTo(bid.getParticipant().getMoney()) > 0) {
          throw new CredibilityNotVerifiedException("Not enough money to pay for all penalties");
        } else {
          PaymentTransferDTO data = new PaymentTransferDTO(
              UUID.randomUUID().toString(), auction.getOriginalId(), bid.getBidSize(), PaymentTransferType.FULL
          );
          try {
            outboxRepository.save(new OutboxRecord(objectMapper.writeValueAsString(data)));
          } catch (JsonProcessingException e) {
            throw new PaymentException("Something went wrong! Please try again later!");
          }
        }
      } else {
        PaymentTransferDTO data = new PaymentTransferDTO(
            UUID.randomUUID().toString(), auction.getOriginalId(), auction.getStart(), PaymentTransferType.PENALTY
        );
        try {
          outboxRepository.save(new OutboxRecord(objectMapper.writeValueAsString(data)));
        } catch (JsonProcessingException e) {
          throw new PaymentException("Something went wrong! Please try again later!");
        }
      }
    }
  }

  @Transactional
  public void finishAuctionByForce(Auction auction) {
    if (auction.getStatus() == Status.AWAIT_SELFSTOP) {
      Optional<Bid> optBid = bidRepository.findWinningBidForAuction(auction.getId());
      if (optBid.isEmpty()) {
        PaymentTransferDTO data = new PaymentTransferDTO(
            UUID.randomUUID().toString(), auction.getOriginalId(), BigDecimal.valueOf(0), PaymentTransferType.AUTO_NONE
        );
        try {
          outboxRepository.save(new OutboxRecord(objectMapper.writeValueAsString(data)));
          auction.setStatus(Status.SELFSTOP_PENDING);
          auctionRepository.save(auction);
        } catch (JsonProcessingException e) {
          throw new IllegalStateException("Whoops! ObjectMapper is on dope again! (Ran into error while auto-ending", e);
        }
      } else {
        PaymentTransferDTO data = new PaymentTransferDTO(
            UUID.randomUUID().toString(),
            auction.getOriginalId(),
            optBid.get().getBidSize(),
            PaymentTransferType.AUTO_PENALTY
        );
        try {
          outboxRepository.save(new OutboxRecord(objectMapper.writeValueAsString(data)));
          auction.setStatus(Status.SELFSTOP_PENDING);
          auctionRepository.save(auction);
        } catch (JsonProcessingException e) {
          throw new IllegalStateException("Whoops! ObjectMapper is on dope again! (Ran into error while getting auto-penalty", e);
        }
      }
    }
  }
}
