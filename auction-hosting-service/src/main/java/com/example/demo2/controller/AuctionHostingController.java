package com.example.demo2.controller;

import com.example.demo2.controller.records.*;
import com.example.demo2.exceptions.ApiError;
import com.example.demo2.exceptions.BadBidRequestException;
import com.example.demo2.models.auction.Auction;
import com.example.demo2.models.auction.AuctionRepository;
import com.example.demo2.models.bid.Bid;
import com.example.demo2.models.bid.BidRepository;
import com.example.demo2.models.participant.Participant;
import com.example.demo2.models.participant.ParticipantRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController("participantController")
@RequestMapping("/api")
public class AuctionHostingController {
  private final ParticipantRepository participantRepository;
  private final AuctionRepository auctionRepository;
  private final BidRepository bidRepository;

  @Autowired
  public AuctionHostingController(ParticipantRepository participantRepository, AuctionRepository auctionRepository, BidRepository bidRepository) {
    this.participantRepository = participantRepository;
    this.auctionRepository = auctionRepository;
    this.bidRepository = bidRepository;
  }

  @Transactional
  @PostMapping("/participant")
  public ParticipantResponseDTO createParticipant(@RequestBody @Valid Participant participant) {
    Participant participantSaved = participantRepository.save(participant);
    return new ParticipantResponseDTO(
        participantSaved.getUsername(), participantSaved.getMoney()
    );
  }

  @Transactional
  @PutMapping("/participant/{id}")
  public void updateParticipant(@PathVariable("id") Long id,
                                @RequestBody @Valid ParticipantUpdateDTO toUpdate) {
    Participant participant = participantRepository.findById(id).orElseThrow();
    participantRepository.save(participant);
  }

  @Transactional
  @PostMapping("/participant/{id}/top-up")
  public ParticipantResponseDTO topUpParticipant(@PathVariable("id") Long id,
                                                 @RequestBody @Valid ParticipantTopUpDTO topUpDTO) {
    Participant participant = participantRepository.findById(id).orElseThrow();
    participant.setMoney(participant.getMoney().add(topUpDTO.topUp()));
    Participant toReturn = participantRepository.save(participant);
    return new ParticipantResponseDTO(toReturn.getUsername(), toReturn.getMoney());
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  @PostMapping("/auction/{id}/bid")
  public BidResponseDTO bidOnAuction(@PathVariable("id") Long id,
                                     @RequestBody @Valid BidRequestDTO bidRequestDTO) {
    Auction auction = auctionRepository.findById(id).orElseThrow();
    Participant participant = participantRepository.findById(bidRequestDTO.participantId()).orElseThrow();
    Optional<Bid> highestBid = bidRepository.findWinningBidForAuction(id);

    if (highestBid.isEmpty() ||
        highestBid.get().getBidSize().add(auction.getStep()).compareTo(bidRequestDTO.bidSize()) == 1) {
      Bid bid = new Bid(bidRequestDTO.bidSize(), auction, participant);
      return new BidResponseDTO("Bid successfully passed.");
    } else {
      throw new BadBidRequestException("Bad bid request: auction min step is " + auction.getStep() +
          ", while your step was only " + bidRequestDTO.bidSize().subtract(highestBid.get().getBidSize()));
    }
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> noSuchElementExceptionHandler(NoSuchElementException e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> constraintViolationExceptionResponse(ConstraintViolationException e) {
    return new ResponseEntity(new ApiError(e.getConstraintViolations()
        .stream().map(ConstraintViolation::getMessage)
        .collect(Collectors.joining(", "))),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> badBidRequestExceptionHandler(BadBidRequestException e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
  }
}
