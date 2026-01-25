package org.example.battingstock.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.battingstock.domain.player.repository.PriceChangeLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyPriceLogCleaner {
    private final PriceChangeLogRepository logRepository;

    // 매일 새벽 4시에 실행
    @Scheduled(cron = "0 0 4 * * *")
    @Transactional
    public void clearDailyLogs() {
        logRepository.deleteAllInBatch();
        log.info("[어제자 시뮬레이션 로그 삭제 완료]");
    }
}
