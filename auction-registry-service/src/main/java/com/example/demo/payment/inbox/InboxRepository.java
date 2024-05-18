package com.example.demo.payment.inbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxRepository extends JpaRepository<InboxRecord, String> {
}
