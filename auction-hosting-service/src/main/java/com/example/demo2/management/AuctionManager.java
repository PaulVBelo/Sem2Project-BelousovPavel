package com.example.demo2.management;

import com.example.demo2.exceptions.AuctionManagementException;
import com.example.demo2.management.records.AuctionLaunchRequestDTO;
import com.example.demo2.management.records.AuctionLaunchResponseDTO;
import com.example.demo2.management.records.AuctionStopRequestDTO;
import com.example.demo2.management.records.AuctionStopResponseDTO;
import com.example.demo2.models.auction.Auction;
import com.example.demo2.models.auction.AuctionRepository;
import com.example.demo2.models.auction.Status;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service("auctionManager")
public class AuctionManager {
  private final AuctionRepository auctionRepository;

  public AuctionManager(AuctionRepository auctionRepository) {
    this.auctionRepository = auctionRepository;
  }

  public AuctionLaunchResponseDTO launchAuction(AuctionLaunchRequestDTO launchRequestDTO) {
    Optional<Auction> optRunningAuction = auctionRepository.findActiveByOldId(launchRequestDTO.auctionId());
    if (optRunningAuction.isEmpty()) {
      LocalDateTime launchTime = LocalDateTime.now();
      Auction auction = new Auction(
          launchRequestDTO.item(),
          launchRequestDTO.step(),
          launchRequestDTO.start(),
          launchTime,
          launchTime.plusSeconds(launchRequestDTO.duration()),
          launchRequestDTO.auctionId()
      );
      Auction auctionSaved = auctionRepository.save(auction);
      return new AuctionLaunchResponseDTO(true, auctionSaved.getOriginalId(), launchTime);
    } else {
      throw new AuctionManagementException("Auction already running/finished! State (status): " +
          optRunningAuction.get().getStatus());
    }
  }

  public AuctionStopResponseDTO stopAuction(AuctionStopRequestDTO stopRequestDTO) {
    Optional<Auction> optRunningAuction = auctionRepository.findActiveByOldId(stopRequestDTO.auctionId());
    if (optRunningAuction.isPresent()) {
      Auction auction = optRunningAuction.get();
      if (auction.getStatus() == Status.RUNNING) {
        auction.setStatus(Status.CANCELED);
        auctionRepository.save(auction);
        return new AuctionStopResponseDTO(true, stopRequestDTO.auctionId());
      } else {
        throw new AuctionManagementException("Auction is in payment/finished state: " +
            auction.getStatus());
      }
    } else {
      throw new AuctionManagementException("Auction already stopped.");
    }
  }
}
