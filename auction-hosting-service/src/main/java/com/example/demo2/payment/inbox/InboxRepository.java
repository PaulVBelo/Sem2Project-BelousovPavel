package com.example.demo2.payment.inbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("inboxRepository")
public interface InboxRepository extends JpaRepository<InboxRecord, String> {
}
