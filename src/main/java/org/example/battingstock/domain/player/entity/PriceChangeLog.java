package org.example.battingstock.domain.player.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "price_change_log")
public class PriceChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerKey;
    private String playerName;

    private String outcome;    // HOMERUN, HIT, BALL, OUT, STRIKEOUT 등
    private Long changedPrice; // 변동 후 가격
    private Long fluctuation;  // 변동 금액

    private LocalDateTime createdAt;

    @Builder
    public PriceChangeLog(String playerKey, String playerName, String outcome, Long changedPrice, Long fluctuation) {
        this.playerKey = playerKey;
        this.playerName = playerName;
        this.outcome = outcome;
        this.changedPrice = changedPrice;
        this.fluctuation = fluctuation;
        this.createdAt = LocalDateTime.now();
    }
}