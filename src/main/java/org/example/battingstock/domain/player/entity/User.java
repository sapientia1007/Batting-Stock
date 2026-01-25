package org.example.battingstock.domain.player.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // 인증
    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    // 보유 현금 (단위 : 원)
    private Long balance;

    // 총 자산 (보유 현금 + 보유 주식 가치)
    @Transient
    private Long totalAsset;

    @Builder
    public User(String name, String email, String password, Long balance) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public void updateBalance(Long amount) {
        this.balance += amount;
    }
}
