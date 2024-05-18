package com.example.demo.payment;

import com.example.demo.exceptions.PaymentException;
import com.example.demo.gateway.launchtime.AuctionLaunchTimeRepository;
import com.example.demo.models.auction.Auction;
import com.example.demo.models.auction.AuctionRepository;
import com.example.demo.models.auction.Status;
import com.example.demo.models.auctioneer.Auctioneer;
import com.example.demo.models.auctioneer.AuctioneerRepository;
import com.example.demo.outbox.OutboxRecord;
import com.example.demo.outbox.OutboxRepository;
import com.example.demo.payment.inbox.InboxRecord;
import com.example.demo.payment.inbox.InboxRepository;
import com.example.demo.payment.records.PaymentResponseDTO;
import com.example.demo.payment.records.PaymentTransferDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentProcessor implements MessageProcessor {
  private final AuctionRepository auctionRepository;
  private final AuctioneerRepository auctioneerRepository;
  private final AuctionLaunchTimeRepository auctionLaunchTimeRepository;
  private final InboxRepository inboxRepository;
  private final OutboxRepository outboxRepository;
  private final ObjectMapper objectMapper;

  @Autowired
  public PaymentProcessor(AuctionRepository auctionRepository,
                          AuctioneerRepository auctioneerRepository,
                          AuctionLaunchTimeRepository auctionLaunchTimeRepository,
                          InboxRepository inboxRepository,
                          OutboxRepository outboxRepository,
                          ObjectMapper objectMapper) {
    this.auctionRepository = auctionRepository;
    this.auctioneerRepository = auctioneerRepository;
    this.auctionLaunchTimeRepository = auctionLaunchTimeRepository;
    this.inboxRepository = inboxRepository;
    this.outboxRepository = outboxRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  @Transactional
  public void process(String message) {
    try {
      PaymentTransferDTO transferDTO = objectMapper.readValue(message, PaymentTransferDTO.class);
      if (inboxRepository.findById(transferDTO.requestId()).isEmpty()) {
        Auction auction = auctionRepository.findById(transferDTO.auctionId()).orElseThrow();

        Auctioneer auctioneer = auction.getAuctioneer();
        auctioneer.setMoney(auctioneer.getMoney().add(transferDTO.money()));
        auction.setStatus(Status.CLOSED);
        auctionRepository.save(auction);
        auctioneerRepository.save(auctioneer);

        InboxRecord inboxRecord = new InboxRecord(
            transferDTO.requestId(), transferDTO.auctionId(), transferDTO.money(), transferDTO.type()
        );
        inboxRepository.save(inboxRecord);

        // Отправка обратно.
        try {
          outboxRepository.save(new OutboxRecord(objectMapper.writeValueAsString(
              new PaymentResponseDTO(
                  UUID.randomUUID().toString(),
                  transferDTO.auctionId(),
                  transferDTO.money(),
                  transferDTO.type()
                  )
          )));
        } catch (JsonProcessingException e) {
          throw new PaymentException("Couldn't process payment: " + e.getMessage());
        }
      } else {
      }
    } catch (JsonProcessingException e) {
      throw new PaymentException("Couldn't process payment: " + e.getMessage());
    }
  }
}
