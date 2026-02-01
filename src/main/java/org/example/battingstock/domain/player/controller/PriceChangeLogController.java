package org.example.battingstock.domain.player.controller;

import lombok.RequiredArgsConstructor;
import org.example.battingstock.domain.player.entity.PriceChangeLog;
import org.example.battingstock.domain.player.repository.PriceChangeLogRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class PriceChangeLogController {
    private final PriceChangeLogRepository logRepository;

    @GetMapping("/latest")
    public List<PriceChangeLog> getLatestLogs() {
        return logRepository.findTop5UniqueHotEvents();
    }
}