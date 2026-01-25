package org.example.battingstock.domain.player.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.battingstock.domain.player.entity.BattingPlayer;
import org.example.battingstock.domain.player.entity.BattingPlayerHistory;
import org.example.battingstock.domain.player.repository.BattingPlayerHistoryRepository;
import org.example.battingstock.domain.player.repository.BattingPlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockSimulationService {

    private final BattingPlayerRepository battingPlayerRepository;
    private final BattingPlayerHistoryRepository playerHistoryRepository;

    @Transactional
    public void simulateBatting() {
        log.info("========= 실시간 주가 시뮬레이션 시작 =========");
        List<BattingPlayer> battingPlayers = battingPlayerRepository.findRandomBattingPlayers();

        for (BattingPlayer player : battingPlayers) {
            List<BattingPlayerHistory> histories = playerHistoryRepository.findBattingPlayerHistoryByPlayerKey(player.getName()+"_"+player.getBirthday()).orElseThrow(() -> new NoSuchElementException("해당 선수가 존재하지 않습니다."));

            long totalPa = histories.stream().mapToLong(BattingPlayerHistory::getPa).sum();
            long totalH = histories.stream().mapToLong(BattingPlayerHistory::getH).sum();
            long totalHr = histories.stream().mapToLong(BattingPlayerHistory::getHr).sum();
            long totalBb = histories.stream().mapToLong(h -> h.getBb() + h.getHp()).sum();
            long totalSo = histories.stream().mapToLong(BattingPlayerHistory::getSo).sum();

            // 주가 변동 (변동폭은 1%~3% 사이 랜덤)
            applyPriceChange(player, totalPa, totalH, totalHr, totalBb, totalSo);
        }

        battingPlayerRepository.saveAll(battingPlayers);
        log.info("========= 주가 시뮬레이션 완료 (현재가 반영됨) =========");
    }

    private void applyPriceChange(BattingPlayer player, long pa, long h, long hr, long bb, long so) {
        double random = Math.random();
        long oldPrice = player.getCurrentPrice();
        double volatility = 0.01 + (Math.random() * 0.02);

        double hrProb = hr / (double) pa; // 홈런 확률
        double hitProb = (h - hr) / (double) pa; // 일반 안타 확률
        double bbProb = bb / (double) pa; // 출루(볼넷/사구) 확률
        double soProb = so / (double) pa; // 삼진 확률

        // 결과 결정 및 로그
        if (random < hrProb) { // 홈런
            long bonus = (long) (oldPrice * volatility * 2.5); // 홈런은 2.5배 가중치
            player.updateCurrentPrice(oldPrice + bonus);
            log.info("[CRITICAL HIT!!] {} 선수 홈런 작렬! 주가 폭등: {}원 (+{}원)", player.getName(), player.getCurrentPrice(), bonus);

        } else if (random < hrProb + hitProb) { // 안타
            long increase = (long) (oldPrice * volatility);
            player.updateCurrentPrice(oldPrice + increase);
            log.info("[HIT] {} 선수 안타! 주가 상승: {}원 (+{}원)", player.getName(), player.getCurrentPrice(), increase);

        } else if (random < hrProb + hitProb + bbProb) { // 볼넷
            log.info("[WALK] {} 선수 볼넷 출루. 주가 유지: {}원", player.getName(), oldPrice);

        } else if (random < hrProb + hitProb + bbProb + soProb) { // 삼진 (하락)
            long decrease = (long) (oldPrice * volatility);
            player.updateCurrentPrice(Math.max(100L, oldPrice - decrease));
            log.info("[STRIKEOUT] {} 선수 삼진.. 주가 하락: {}원 (-{}원)", player.getName(), player.getCurrentPrice(), decrease);

        } else { // 일반 아웃
            long decrease = (long) (oldPrice * volatility * 0.5);
            player.updateCurrentPrice(Math.max(100L, oldPrice - decrease));
            log.info("[OUT] {} 선수 땅볼 아웃. 주가 하락: {}원 (-{}원)", player.getName(), player.getCurrentPrice(), decrease);
        }
    }
}