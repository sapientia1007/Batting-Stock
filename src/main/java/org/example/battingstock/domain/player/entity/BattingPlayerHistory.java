package org.example.battingstock.domain.player.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "batting_player_history")
public class BattingPlayerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String team;
    private String position;
    private Integer year;
    private String playerKey;

    // 주가 결정 및 핵심 지표
    private Double war;
    private Double avg;
    private Double ops;
    private Double wrcPlus;

    // 카운트 지표
    private Integer g;        // 경기수
    private Integer pa;       // 타석 (출루율 분모)
    private Integer ab;       // 타수 (타율, 장타율 분모)
    private Integer h;        // 안타
    private Integer b2;       // 2루타
    private Integer b3;       // 3루타
    private Integer hr;       // 홈런
    private Integer rbi;      // 타점
    private Integer r;        // 득점
    private Integer sb;       // 도루 성공
    private Integer cs;       // 도루 실패
    private Integer bb;       // 볼넷
    private Integer hp;       // 사구
    private Integer so;       // 삼진
    private Integer gdp;      // 병살타
    private Integer sf;       // 희생플라이
    private Integer sh;       // 희생번트

    @Builder
    public BattingPlayerHistory(String name, String team, String position, Integer year, String playerKey,
                                Double war, Double avg, Double ops, Double wrcPlus, Integer g, Integer pa,
                                Integer ab, Integer h, Integer b2, Integer b3, Integer hr, Integer rbi,
                                Integer r, Integer sb, Integer cs, Integer bb, Integer hp, Integer so,
                                Integer gdp, Integer sf, Integer sh) {
        this.name = name;
        this.team = team;
        this.position = position;
        this.year = year;
        this.playerKey = playerKey;
        this.war = war;
        this.avg = avg;
        this.ops = ops;
        this.wrcPlus = wrcPlus;
        this.g = g;
        this.pa = pa;
        this.ab = ab;
        this.h = h;
        this.b2 = b2;
        this.b3 = b3;
        this.hr = hr;
        this.rbi = rbi;
        this.r = r;
        this.sb = sb;
        this.cs = cs;
        this.bb = bb;
        this.hp = hp;
        this.so = so;
        this.gdp = gdp;
        this.sf = sf;
        this.sh = sh;
    }
}