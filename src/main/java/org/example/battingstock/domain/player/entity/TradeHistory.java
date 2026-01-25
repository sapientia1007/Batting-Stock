package org.example.battingstock.domain.player.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.battingstock.domain.player.entity.enums.TradeType;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "trade_history")
public class TradeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private BattingPlayer player;

    private Integer quantity; // 거래 수량
    private Long tradePrice;  // 거래 시점 가격

    @Enumerated(EnumType.STRING)
    private TradeType type;   // BUY, SELL, CANCEL

    private Long totalTradeAmount; // 총 거래 금액 (quantity * tradePrice)
    private Long remainingBalance; // 거래 후 잔액

    private LocalDateTime createdAt; // 거래 일시

    @Builder
    public TradeHistory(User user, BattingPlayer player, Integer quantity, Long tradePrice, TradeType type,  Long totalTradeAmount, Long remainingBalance) {
        this.user = user;
        this.player = player;
        this.quantity = quantity;
        this.tradePrice = tradePrice;
        this.type = type;
        this.totalTradeAmount = totalTradeAmount;
        this.remainingBalance = remainingBalance;
        this.createdAt = LocalDateTime.now();
    }
}
