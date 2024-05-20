package com.example.demo2.payment;

import com.example.demo2.models.auction.Auction;
import com.example.demo2.models.auction.AuctionRepository;
import com.example.demo2.models.auction.Status;
import com.example.demo2.models.bid.Bid;
import com.example.demo2.models.bid.BidRepository;
import com.example.demo2.models.participant.Participant;
import com.example.demo2.models.participant.ParticipantRepository;
import com.example.demo2.payment.inbox.InboxRecord;
import com.example.demo2.payment.inbox.InboxRepository;
import com.example.demo2.payment.records.PaymentResponseDTO;
import com.example.demo2.payment.records.PaymentTransferType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentListener {
  private final AuctionRepository auctionRepository;
  private final BidRepository bidRepository;
  private final ParticipantRepository participantRepository;
  private final InboxRepository inboxRepository;
  private final ObjectMapper objectMapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(PaymentListener.class);
  public PaymentListener(AuctionRepository auctionRepository,
                         BidRepository bidRepository,
                         ParticipantRepository participantRepository,
                         InboxRepository inboxRepository,
                         ObjectMapper objectMapper) {
    this.auctionRepository = auctionRepository;
    this.bidRepository = bidRepository;
    this.participantRepository = participantRepository;
    this.inboxRepository = inboxRepository;
    this.objectMapper = objectMapper;
  }

  @KafkaListener(topics = {"${payment.topic-to-consume-message}"})
  @Transactional
  public void consumeMessage(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
    PaymentResponseDTO responseDTO = objectMapper.readValue(message, PaymentResponseDTO.class);
    acknowledgment.acknowledge();
    if (inboxRepository.findById(responseDTO.responseId()).isEmpty()) {
      inboxRepository.save(new InboxRecord(
          responseDTO.responseId(), responseDTO.auctionId(), responseDTO.money(), responseDTO.type()
          ));
      Auction auction = auctionRepository.findActiveByOldId(responseDTO.auctionId()).orElseThrow();
      if (responseDTO.type() != PaymentTransferType.AUTO_NONE) {
        Bid bid = bidRepository.findWinningBidForAuction(auction.getId()).orElseThrow();
        Participant participant = bid.getParticipant();
        if (responseDTO.type() == PaymentTransferType.FULL) {
          LOGGER.info("Participant " + participant.getUsername() +
              " has received item " + auction.getItem());
        } else {
          LOGGER.info("Participant " + participant.getUsername() +
              " has payed for disrupting auction #" + auction.getId() +
              ", aka auction #" + auction.getOriginalId() + " from registry.");
        }
      } else {
        LOGGER.info("Auction #" + auction.getId() +
            ", aka auction #" + auction.getOriginalId() +
            " from registry, ended with no participants :(");
      }
      auction.setStatus(Status.CLOSED);
      auctionRepository.save(auction);
      // Конец программы :D
    }
  }
}
