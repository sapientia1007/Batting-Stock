package org.example.battingstock.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.battingstock.domain.player.entity.BattingPlayer;
import org.example.battingstock.domain.player.repository.BattingPlayerRepository;
import org.example.battingstock.domain.player.repository.PriceChangeLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyPriceLogCleaner {
    private final PriceChangeLogRepository logRepository;
    private final BattingPlayerRepository battingPlayerRepository;

    // 매일 새벽 2시에 실행 : 어제자 라인업 및 시뮬 로그 삭제
//    @Scheduled(cron = "0 0 2 * * *")
    @Scheduled(cron = "0 07 21 * * *") // 테스트용
    @Transactional
    public void clearDailyLogs() {
        logRepository.deleteAllInBatch();
        battingPlayerRepository.deactivateAllStarters();
        log.info("******** [어제자 시뮬레이션 로그 삭제 완료] ********");
    }

    // 매일 새벽 3시에 실행 : 오늘의 라인업 (각 팀별, 포지션별)
//    @Scheduled(cron = "0 0 3 * * *")
    @Scheduled(cron = "0 08 21 * * *") // 테스트용
    @Transactional
    public void makeDailyLineUp() {
        log.info("******** [오늘자 시뮬레이션 라인업 생성] ********");
        List<String> teams = List.of("삼성", "KT", "KIA", "NC", "두산", "한화", "LG", "키움", "롯데");

        // 팀별로 돌면서 포지션별 랜덤 1명씩 주전
        for (String team : teams) {
            List<BattingPlayer> starters = battingPlayerRepository.findRandomBattingPlayers(team);
            String lineUpNames = starters.stream()
                    .map(p -> "[" + p.getPosition() + ": " + p.getName() + "]")
                    .collect(Collectors.joining(", "));

            log.info("==== [{}] 오늘의 라인업: {} ====", team, lineUpNames);
            starters.forEach(BattingPlayer::updateStarter);
        }
    }
}
