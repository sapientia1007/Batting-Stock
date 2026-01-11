package org.example.battingstock.domain.player.service;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.battingstock.domain.player.dto.StatAccumulator;
import org.example.battingstock.domain.player.entity.BattingPlayer;
import org.example.battingstock.domain.player.entity.BattingPlayerHistory;
import org.example.battingstock.domain.player.repository.BattingPlayerHistoryRepository;
import org.example.battingstock.domain.player.repository.BattingPlayerRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattingPlayerHistoryDataLoader {

    private final BattingPlayerHistoryRepository battingPlayerHistoryRepository;
    private final BattingPlayerRepository battingPlayerRepository;

    @Transactional
    public void loadKboHistoryData() {
        // 중복방지
        if (battingPlayerRepository.count() > 0) return;

        Map<String, StatAccumulator> accumulatorMap = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource("kbo_batting_stats_by_season_1982-2025.csv").getInputStream(), "UTF-8"))) {
            List<BattingPlayerHistory> players = new ArrayList<>();
            String[] line;
            reader.readNext(); // 헤더 제외

            while((line = reader.readNext()) != null){
                int year = Integer.parseInt(line[6]);
                if (year >= 2020 && year <= 2025) {
                    double war = Double.parseDouble(line[38]);

                    String birthday = line[2].replaceAll("[^0-9]", "");
                    String playerKey = line[1]+"_"+birthday;

                    BattingPlayerHistory battingPlayerHistory = BattingPlayerHistory.builder()
                            .name(line[1])
                            .team(line[7])
                            .position(line[9])
                            .year(year)
                            .playerKey(playerKey)
                            .war(war)
                            .avg(parseDoubleSafe(line[32]))
                            .ops(parseDoubleSafe(line[35]))
                            .wrcPlus(parseDoubleSafe(line[37]))
                            .g(parseIntSafe(line[10]))
                            .pa(parseIntSafe(line[13]))
                            .ab(parseIntSafe(line[15]))
                            .h(parseIntSafe(line[17]))
                            .b2(parseIntSafe(line[18]))
                            .b3(parseIntSafe(line[19]))
                            .hr(parseIntSafe(line[20]))
                            .rbi(parseIntSafe(line[22]))
                            .r(parseIntSafe(line[16]))
                            .sb(parseIntSafe(line[23]))
                            .cs(parseIntSafe(line[24]))
                            .bb(parseIntSafe(line[25]))
                            .hp(parseIntSafe(line[26]))
                            .so(parseIntSafe(line[28]))
                            .gdp(parseIntSafe(line[29]))
                            .sf(parseIntSafe(line[31]))
                            .sh(parseIntSafe(line[30]))
                            .build();
                    players.add(battingPlayerHistory);

                    StatAccumulator acc = accumulatorMap.get(playerKey);

                    if (acc == null) {
                        acc = new StatAccumulator(line[1], line[7], line[9], year);
                        accumulatorMap.put(playerKey, acc);
                    }

                    acc.addStats(battingPlayerHistory.getWar(), battingPlayerHistory.getAvg(), battingPlayerHistory.getOps(), battingPlayerHistory.getHr(),
                            battingPlayerHistory.getYear(), battingPlayerHistory.getTeam());
                }
            }
            battingPlayerHistoryRepository.saveAll(players);

            List<BattingPlayer> battingPlayers = new ArrayList<>();
            for (StatAccumulator acc : accumulatorMap.values()) {
                BattingPlayer battingPlayer = BattingPlayer.builder()
                        .name(acc.getName())
                        .team(acc.getTeam())
                        .position(acc.getPosition())
                        .war(acc.getAverageWar())
                        .avg(acc.getAverageAvg())
                        .hr(acc.getAverageHr())
                        .ops(acc.getAverageOps())
                        .basePrice(calculateInitialPrice(acc.getAverageWar()))
                        .build();
                battingPlayers.add(battingPlayer);
            }
            battingPlayerRepository.saveAll(battingPlayers);

        } catch (Exception e) {
            throw new RuntimeException("CSV 로딩 중 오류 발생: " + e.getMessage());
        }
    }

    private Long calculateInitialPrice(Double war) {
        // 상장가 계산: 평균 WAR * 10,000원 (최소 1,000원 보장)
        return Math.max(1000L, (long) (war * 10000));
    }

    private double parseDoubleSafe(String value) {
        if (value == null || value.trim().isEmpty() || value.equals("-")) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseIntSafe(String value) {
        if (value == null || value.trim().isEmpty() || value.equals("-")) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void checkCsvData() {
        String fileName = "kbo_batting_stats_by_season_1982-2025.csv";
        log.info("==== CSV 데이터 읽기 테스트 시작 ====");

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource(fileName).getInputStream(), StandardCharsets.UTF_8))) {

            String[] line;
            String[] header = reader.readNext();

            if (header != null) {
                log.info("총 컬럼 수: {}", header.length);
                for (int i = 0; i < header.length; i++) {
                    log.info("인덱스 [{}]: {}", i, header[i]);
                }
            }

            log.info("------------------------------------------");

            int count = 0;
            while ((line = reader.readNext()) != null && count < 10) {
                try {
                    int year = Integer.parseInt(line[6]);

                    if (year >= 2020 && year <= 2025) {
                        String name = line[1];
                        String team = line[7];
                        String avg = line[32];
                        String hr = line[20];
                        String war = line[38];

                        log.info("[데이터 {}] 연도: {} | 이름: {} | 팀: {} | 타율: {} | 홈런: {} | WAR: {}",
                                count + 1, year, name, team, avg, hr, war);
                        count++;
                    }
                } catch (Exception e) {
                    log.warn("줄 무시 (파싱 에러): {}", e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("CSV 로딩 중 오류 발생: {}", e.getMessage());
        }
        log.info("==== CSV 데이터 읽기 테스트 종료 ====");
    }
}
