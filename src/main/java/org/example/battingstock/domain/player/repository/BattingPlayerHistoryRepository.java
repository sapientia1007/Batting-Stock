package org.example.battingstock.domain.player.repository;

import org.example.battingstock.domain.player.entity.BattingPlayerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattingPlayerHistoryRepository extends JpaRepository<BattingPlayerHistory, Long> {
}
