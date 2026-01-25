package org.example.battingstock.domain.player.repository;

import org.example.battingstock.domain.player.entity.PriceChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceChangeLogRepository extends JpaRepository<PriceChangeLog, Long> {
}
