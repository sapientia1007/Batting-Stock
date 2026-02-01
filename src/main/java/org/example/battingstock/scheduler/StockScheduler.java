package org.example.battingstock.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.battingstock.domain.player.service.StockSimulationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockScheduler implements CommandLineRunner {
    private final StockSimulationService stockSimulationService;
    private final TaskScheduler taskScheduler;

    private void scheduleNextUpdate() {
        // 랜덤 시간 계산
        long delay = 5000 + (long) (Math.random() * 10000);

        log.info("다음 시뮬레이션까지 대기 시간: {}ms", delay);

        // 예약 실행
        taskScheduler.schedule(() -> {
            try {
                stockSimulationService.simulateBatting();
            } finally {
                // 실행이 끝나면 다시 다음 랜덤 실행을 예약
                scheduleNextUpdate();
            }
        }, Instant.now().plusMillis(delay));
    }

    @Override
    public void run(String... args) throws Exception {
        scheduleNextUpdate();
    }
}
