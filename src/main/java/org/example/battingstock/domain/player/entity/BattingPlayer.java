package org.example.battingstock.domain.player.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "players")
public class BattingPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String birthday;
    private String team;
    private String position;

    private Double war;
    private Double avg;
    private Integer hr;
    private Double ops;

    // 실시간 주가 정보
    private Long currentPrice; // 현재가
    private Long basePrice;    // 시초가

    @Builder
    public BattingPlayer(String name, String birthday, String team, String position,
                         Double war, Double avg, Integer hr, Double ops, Long basePrice) {
        this.name = name;
        this.birthday = birthday;
        this.team = team;
        this.position = position;
        this.war = war;
        this.avg = avg;
        this.hr = hr;
        this.ops = ops;
        this.basePrice = basePrice;
        this.currentPrice = basePrice;
    }

    public Long updateCurrentPrice(Long updatedPrice) {
        this.currentPrice = updatedPrice;
        return updatedPrice;
    }

}
