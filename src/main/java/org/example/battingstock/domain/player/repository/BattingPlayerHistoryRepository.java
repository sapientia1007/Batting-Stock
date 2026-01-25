package org.example.battingstock.domain.player.repository;

import org.example.battingstock.domain.player.entity.BattingPlayerHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BattingPlayerHistoryRepository extends JpaRepository<BattingPlayerHistory, Long> {
    Optional<List<BattingPlayerHistory>> findBattingPlayerHistoryByPlayerKey(String playerKey);
}
