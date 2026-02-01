package org.example.battingstock.domain.player.repository;

import org.example.battingstock.domain.player.entity.PriceChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PriceChangeLogRepository extends JpaRepository<PriceChangeLog, Long> {

    // 오늘 발생한 로그 중 변동액 절대값이 큰 순서대로 5개
    @Query(value = "SELECT * FROM price_change_log " +
            "WHERE id IN (SELECT MAX(id) FROM price_change_log GROUP BY player_key) " +
            "ORDER BY ABS(fluctuation) DESC LIMIT 5", nativeQuery = true)
    List<PriceChangeLog> findTop5UniqueHotEvents();
}