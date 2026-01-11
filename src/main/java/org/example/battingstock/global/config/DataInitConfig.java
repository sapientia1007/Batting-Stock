package org.example.battingstock.global.config;

import org.example.battingstock.domain.player.service.BattingPlayerHistoryDataLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitConfig {
    @Bean
    public CommandLineRunner initData(BattingPlayerHistoryDataLoader dataLoader) {
        return args -> {
            dataLoader.checkCsvData();

            dataLoader.loadKboHistoryData();
        };
    }
}
