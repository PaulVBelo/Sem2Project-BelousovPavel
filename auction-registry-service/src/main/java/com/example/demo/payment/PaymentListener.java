package com.example.demo.payment;

import com.example.demo.payment.records.PaymentTransferDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentListener {
  private final ObjectMapper objectMapper;
  private final MessageProcessor processor;

  @Autowired
  public PaymentListener(ObjectMapper objectMapper, MessageProcessor processor) {
    this.objectMapper = objectMapper;
    this.processor = processor;
  }

  @KafkaListener(topics = {"${payment.topic-to-consume-message}"})
  @Transactional
  public void consumeMessage(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
    var result = objectMapper.readValue(message, PaymentTransferDTO.class);
    processor.process(message);
    acknowledgment.acknowledge();
  }
}
