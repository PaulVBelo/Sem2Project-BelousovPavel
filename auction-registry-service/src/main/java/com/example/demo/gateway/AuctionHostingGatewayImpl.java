package com.example.demo.gateway;

import com.example.demo.exceptions.AuctionLaunchException;
import com.example.demo.exceptions.AuctionStopException;
import com.example.demo.gateway.records.AuctionLaunchRequestDTO;
import com.example.demo.gateway.records.AuctionLaunchResponseDTO;
import com.example.demo.gateway.records.AuctionStopRequestDTO;
import com.example.demo.gateway.records.AuctionStopResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service("auctionHostingGateway")
@ConditionalOnProperty(value = "auction-hosting-gateway.mode", havingValue = "http")
public class AuctionHostingGatewayImpl implements AuctionHostingGateway{
  private final RestTemplate restTemplate;

  @Autowired
  public AuctionHostingGatewayImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  @Override
  @RateLimiter(name = "launchAuction")
  @CircuitBreaker(name = "launchAuction")
  @Retry(name = "launchAuction")
  public AuctionLaunchResponseDTO launchAuction(AuctionLaunchRequestDTO toLaunch, String requestId) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-REQUEST-ID", requestId);
      ResponseEntity<AuctionLaunchResponseDTO> response =
          restTemplate.exchange(
              "/api/auction/launch",
              HttpMethod.POST,
              new HttpEntity<>(
                Map.of(
                  "auctionId", toLaunch.auctionId(),
                  "item", toLaunch.item(),
                  "step", toLaunch.step(),
                  "start", toLaunch.start(),
                  "duration", toLaunch.duration()
                ),
                  headers),
              new ParameterizedTypeReference<>() {
              }
          );
      return response.getBody();
    } catch (RestClientException e) {
      throw new AuctionLaunchException("Couldn't launch auction: " + e.getMessage());
    }
  }

  @Override
  @RateLimiter(name = "stopAuction")
  @CircuitBreaker(name = "stopAuction")
  @Retry(name = "stopAuction")
  public AuctionStopResponseDTO stopAuction(AuctionStopRequestDTO toStop, String requestId) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.add("X-REQUEST-ID", requestId);
      ResponseEntity<AuctionStopResponseDTO> response =
          restTemplate.exchange(
              "/api/auction/stop",
              HttpMethod.POST,
              new HttpEntity<>(
                  Map.of(
                      "auctionId", toStop.auctionId()
                  ),
                  headers),
              new ParameterizedTypeReference<>() {
              }
          );
      return response.getBody();
    } catch (RestClientException e) {
      throw new AuctionStopException("Couldn't launch auction: " + e.getMessage());
    }
  }
}
