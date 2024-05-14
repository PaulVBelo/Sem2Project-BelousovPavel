package com.example.demo2.management.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("stopHistoryRepository")
public interface StopHistoryRepository extends JpaRepository<StopHistoryRecord, String> {
}
