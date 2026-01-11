package org.example.battingstock.domain.player.repository;

import org.example.battingstock.domain.player.entity.BattingPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattingPlayerRepository extends JpaRepository<BattingPlayer, Long> {
}
