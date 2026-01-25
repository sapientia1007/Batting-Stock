package org.example.battingstock.domain.player.repository;

import org.example.battingstock.domain.player.entity.BattingPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BattingPlayerRepository extends JpaRepository<BattingPlayer, Long> {

    // MySQL 기준으로 랜덤 120명 추출
    @Query(value = "SELECT * FROM players ORDER BY RAND() LIMIT 120", nativeQuery = true)
    List<BattingPlayer> findRandomBattingPlayers();
}
