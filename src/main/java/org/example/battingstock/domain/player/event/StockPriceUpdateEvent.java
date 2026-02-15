package org.example.battingstock.domain.player.event;

import org.example.battingstock.domain.player.entity.PriceChangeLog;

import java.util.List;

public record StockPriceUpdateEvent(List<PriceChangeLog> latestLogs) {
}
