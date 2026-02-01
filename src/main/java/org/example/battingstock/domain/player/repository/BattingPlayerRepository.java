package org.example.battingstock.domain.player.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.battingstock.domain.player.entity.BattingPlayer;
import org.example.battingstock.domain.player.entity.dto.PlayerStatDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BattingPlayerRepository extends JpaRepository<BattingPlayer, Long> {

    // 팀+포지션 별로 각 팀당 9명씩 주전 출전
    @Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY position ORDER BY RAND()) as row_num " +
            "from players where team = :team) " +
            "as ranked_players where row_num = 1",
            nativeQuery = true)
    List<BattingPlayer> findRandomBattingPlayers(@Param("team") String team);

    // 주전 초기화
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE BattingPlayer p set p.isTodayStarter = false")
    int deactivateAllStarters();

    // 선수별 데이터
    @Query("SELECT h.playerKey, SUM(h.pa), SUM(h.h), SUM(h.hr), SUM(h.bb + h.hp), SUM(h.so) " +
            "FROM BattingPlayerHistory h " +
            "WHERE h.playerKey IN :playerKeys " +
            "GROUP BY h.playerKey")
    List<Object[]> findStatsByPlayerKeys(@Param("playerKeys") List<String> playerKeys);

    @Query("SELECT new org.example.battingstock.domain.player.entity.dto.PlayerStatDto(" +
            "h.playerKey, h.name, h.team, SUM(h.pa), SUM(h.h), SUM(h.hr), SUM(h.bb + h.hp), SUM(h.so)) " +
            "FROM BattingPlayerHistory h " +
            "WHERE h.playerKey IN :playerKeys " +
            "GROUP BY h.playerKey, h.name, h.team")
    List<PlayerStatDto> findAllStatsByPlayerKeys(@Param("playerKeys") List<String> playerKeys);

    List<BattingPlayer> findBattingPlayerByIsTodayStarterTrue();

    Optional<BattingPlayer> findBattingPlayerByPlayerKey(@Param("playerKey") String playerKey);
}
