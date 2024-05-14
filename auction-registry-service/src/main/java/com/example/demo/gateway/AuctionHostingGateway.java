package com.example.demo.gateway;

import com.example.demo.gateway.records.AuctionLaunchRequestDTO;
import com.example.demo.gateway.records.AuctionLaunchResponseDTO;
import com.example.demo.gateway.records.AuctionStopRequestDTO;
import com.example.demo.gateway.records.AuctionStopResponseDTO;

public interface AuctionHostingGateway {
  public AuctionLaunchResponseDTO launchAuction(AuctionLaunchRequestDTO toLaunch, String requestId);
  public AuctionStopResponseDTO stopAuction(AuctionStopRequestDTO toStop, String requestId);
}
