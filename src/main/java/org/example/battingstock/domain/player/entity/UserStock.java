package org.example.battingstock.domain.player.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_stock")
public class UserStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private BattingPlayer player;

    private Integer quantity;      // 보유 수량
    private Long averagePrice;     // 평단가

    @Builder
    public UserStock(User user, BattingPlayer player, Integer quantity, Long averagePrice) {
        this.user = user;
        this.player = player;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    // 추가 매수 시 평단가를 업데이트하는 비즈니스 로직
    public void updateAveragePrice(int newQuantity, long newPrice) {
        long totalCost = (this.averagePrice * this.quantity) + (newPrice * newQuantity);
        this.quantity += newQuantity;
        this.averagePrice = totalCost / this.quantity;
    }
}
