package com.example.demo.controller;

import com.example.demo.controller.records.*;
import com.example.demo.exceptions.ApiError;
import com.example.demo.models.auction.Auction;
import com.example.demo.models.auction.AuctionRepository;
import com.example.demo.models.auctioneer.Auctioneer;
import com.example.demo.models.auctioneer.AuctioneerRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController("auctioneerController")
@RequestMapping("/api")
public class AuctionRegistryController {
  private final AuctioneerRepository auctioneerRepository;
  private final AuctionRepository auctionRepository;
  @Autowired
  public AuctionRegistryController(AuctioneerRepository auctioneerRepository,
                                   AuctionRepository auctionRepository) {
    this.auctioneerRepository = auctioneerRepository;
    this.auctionRepository = auctionRepository;
  }

  @Transactional
  @PostMapping("/auctioneer")
  public AuctioneerResponseDTO createAuctioneer(@RequestBody @Valid Auctioneer auctioneer) {
    Auctioneer auctioneerSaved = auctioneerRepository.save(auctioneer);
    return new AuctioneerResponseDTO(
        auctioneerSaved.getId(), auctioneerSaved.getUsername(), auctioneerSaved.getMoney()
    );
  }

  @Transactional
  @PutMapping("/auctioneer/{id}")
  public void updateAuctioneer(@PathVariable("id") Long id,
                               @RequestBody @Valid AuctioneerUpdateDTO toUpdate) {
    Auctioneer auctioneer = auctioneerRepository.findById(id).orElseThrow();
    auctioneer.setUsername(toUpdate.username());
    auctioneerRepository.save(auctioneer);
  }

  @Transactional
  @PostMapping("/auction")
  public AuctionResponseDTO createAuctionDraft(@RequestBody @Valid AuctionCreateRequestDTO toCreate) {
    Auctioneer auctioneer = auctioneerRepository.findById(toCreate.auctioneerId()).orElseThrow();
    Auction auction = new Auction(
        toCreate.item(),
        auctioneer,
        toCreate.step(),
        toCreate.start(),
        toCreate.duration()
    );
    Auction auctionSaved = auctionRepository.save(auction);
    return new AuctionResponseDTO(
        auctionSaved.getItem(),
        auctioneer.getUsername(),
        auctionSaved.getStep(),
        auctionSaved.getStart(),
        auctionSaved.getDuration()
    );
  }

  @Transactional
  @PutMapping("/auction/{id}")
  public void editAuctionDraft(@PathVariable("id") Long id,
                               @RequestBody @Valid AuctionUpdateDTO toUpdate) {
    Auction auction = auctionRepository.findById(id).orElseThrow();
    auction.setItem(toUpdate.item());
    auction.setStep(toUpdate.step());
    auction.setStart(toUpdate.start());
    auction.setDuration(toUpdate.duration());
    auctionRepository.save(auction);
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
}
