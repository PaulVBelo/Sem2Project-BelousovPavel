package com.example.demo.gateway;

import com.example.demo.exceptions.ApiError;
import com.example.demo.exceptions.AuctionLaunchException;
import com.example.demo.exceptions.AuctionStopException;
import com.example.demo.gateway.records.AuctionLaunchRequestDTO;
import com.example.demo.gateway.records.AuctionLaunchResponseDTO;
import com.example.demo.gateway.records.AuctionStopRequestDTO;
import com.example.demo.gateway.records.AuctionStopResponseDTO;
import com.example.demo.models.auction.Auction;
import com.example.demo.models.auction.AuctionRepository;
import com.example.demo.models.auction.Status;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController("auctionManagementController")
@RequestMapping("/api/auction")
public class AuctionManagementController {

  private final AuctionRepository auctionRepository;
  private final AuctionHostingGateway gateway;

  public AuctionManagementController(AuctionRepository auctionRepository, AuctionHostingGateway gateway) {
    this.auctionRepository = auctionRepository;
    this.gateway = gateway;
  }

  @Transactional
  @PostMapping("/{id}/launch")
  public AuctionLaunchResponseDTO launchAuction(@PathVariable("id") Long id) {
    Auction auction = auctionRepository.findById(id).orElseThrow();
    if (auction.getStatus() == Status.DRAFT) {
      AuctionLaunchResponseDTO responseDTO =
          gateway.launchAuction(new AuctionLaunchRequestDTO(
                  auction.getId(),
                  auction.getItem(),
                  auction.getStep(),
                  auction.getStart(),
                  auction.getDuration()
              ),
              UUID.randomUUID().toString()
          );
      if (responseDTO.isLaunched()) {
        auction.setStatus(Status.RUNNING);
        auctionRepository.save(auction);
        // !!! Добавить сохранение времени запуска, можно в таблице аукционов, можно отдельно.
        // Это полезно скорее для фронтенда, нежели работы микросервиса. UI не будет, но нужно оставить узлы для расширения.
      }
      return responseDTO;
    } else {
      // Не должно происходить, но пусть будет.
      throw new AuctionLaunchException("Cannot launch auction, that is active/finished");
    }
  }

  @Transactional
  @PostMapping("/{id}/stop")
  public AuctionStopResponseDTO stopAuction(@PathVariable("id") Long id) {
    Auction auction = auctionRepository.findById(id).orElseThrow();
    if (auction.getStatus() == Status.RUNNING) {
      AuctionStopResponseDTO responseDTO =
          gateway.stopAuction(new AuctionStopRequestDTO(auction.getId()), UUID.randomUUID().toString());
      if (responseDTO.isStopped()) {
        auction.setStatus(Status.DRAFT);
        auctionRepository.save(auction);
        // !!! Здесь удаляем время запуска из истории.
      }
      return responseDTO;
    } else {
      // Не должно происходить, но пусть будет.
      // В том плане, что кнопка пользователю не отображается... В UI которого нет... Проехали.
      throw new AuctionStopException("Auction to be stopped wasn't running");
    }
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> noSuchElementExceptionHandler(NoSuchElementException e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> auctionLaunchExceptionHandler(AuctionLaunchException e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> auctionStopExceptionHandler(AuctionStopException e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> requestNotPermittedResponse(RequestNotPermitted e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> callNotPermittedExceptionResponse(CallNotPermittedException e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.BAD_GATEWAY);
  }
}
