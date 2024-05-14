package com.example.demo2.management;

import com.example.demo2.exceptions.ApiError;
import com.example.demo2.exceptions.AuctionManagementException;
import com.example.demo2.management.models.LaunchHistoryRecord;
import com.example.demo2.management.models.LaunchHistoryRepository;
import com.example.demo2.management.models.StopHistoryRecord;
import com.example.demo2.management.models.StopHistoryRepository;
import com.example.demo2.management.records.AuctionLaunchRequestDTO;
import com.example.demo2.management.records.AuctionLaunchResponseDTO;
import com.example.demo2.management.records.AuctionStopRequestDTO;
import com.example.demo2.management.records.AuctionStopResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auction")
public class AuctionManagementService {
  private final LaunchHistoryRepository launchHistoryRepository;
  private final StopHistoryRepository stopHistoryRepository;
  private final AuctionManager manager;

  @Autowired
  public AuctionManagementService(LaunchHistoryRepository launchHistoryRepository,
                                  StopHistoryRepository stopHistoryRepository,
                                  AuctionManager manager) {
    this.launchHistoryRepository = launchHistoryRepository;
    this.stopHistoryRepository = stopHistoryRepository;
    this.manager = manager;
  }

  @Transactional
  @PostMapping("/launch")
  public AuctionLaunchResponseDTO launchAuction(
      @RequestBody @Valid AuctionLaunchRequestDTO auctionToLaunch,
      @NotNull @RequestHeader("X-REQUEST-ID") String requestId) {

    Optional<LaunchHistoryRecord> optRecord = launchHistoryRepository.findById(requestId);
    if (optRecord.isEmpty()) {
      AuctionLaunchResponseDTO responseDTO = manager.launchAuction(auctionToLaunch);
      launchHistoryRepository.save(new LaunchHistoryRecord(
          requestId, responseDTO.isLaunched(), responseDTO.auctionId(), responseDTO.launchTime()
          ));
      return responseDTO;
    } else {
      LaunchHistoryRecord record = optRecord.get();
      return new AuctionLaunchResponseDTO(record.getSuccess(), record.getOriginalAuctionId(), record.getLaunchTime());
    }
  }

  @Transactional
  @PostMapping("/stop")
  public AuctionStopResponseDTO stopAuction(@RequestBody @Valid AuctionStopRequestDTO requestDTO,
                                            @NotNull @RequestHeader("X-REQUEST-ID") String requestId) {
    Optional<StopHistoryRecord> prevHistoryRecord = stopHistoryRepository.findById(requestId);
    if (prevHistoryRecord.isEmpty()) {
      AuctionStopResponseDTO responseDTO = manager.stopAuction(requestDTO);
      stopHistoryRepository.save(new StopHistoryRecord(requestId, true));
      return responseDTO;
    } else {
      return new AuctionStopResponseDTO(prevHistoryRecord.get().getSuccess(), requestDTO.auctionId());
    }
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> auctionManagementExceptionHandler(AuctionManagementException e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
  }
}
