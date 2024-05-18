package com.example.demo2.payment;

import com.example.demo2.exceptions.ApiError;
import com.example.demo2.exceptions.CredibilityNotVerifiedException;
import com.example.demo2.exceptions.PaymentException;
import com.example.demo2.payment.records.PaymentRequestDTO;
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

@RestController("paymentController")
@RequestMapping("/api/payment")
public class PaymentController {
  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @Transactional
  @PutMapping("/full")
  public void payForAuction(@RequestBody @Valid PaymentRequestDTO request){
    paymentService.prepareAuctionForPayment(request, true);
  }

  @Transactional
  @PutMapping("/penalty")
  public void cancelParticipation(@RequestBody @Valid PaymentRequestDTO request) {
    paymentService.prepareAuctionForPayment(request, false);
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
  public ResponseEntity<ApiError> credibilityNotVerifiedExceptionHandler(CredibilityNotVerifiedException e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ApiError> illegalPaymentRequestHandler(PaymentException e) {
    return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.FORBIDDEN);
  }
}
