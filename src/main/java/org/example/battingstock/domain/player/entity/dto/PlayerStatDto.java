package org.example.battingstock.domain.player.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerStatDto {
    private String playerKey;
    private String playerName;
    private String playerTeam;
    private long totalPa;
    private long totalH;
    private long totalHr;
    private long totalBb;
    private long totalSo;
}
